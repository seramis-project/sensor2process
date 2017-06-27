package at.ac.wu.seramis.sensor2process.utils;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.internal.EntryDefault;

public class NearestNeighboursDiTe
{
	private RTree<String, Point> _tree = RTree.create();
	private TreeMap<String, Entry<String, Point>> _treeEntries = new TreeMap<>();
	private TreeMap<String, String> _itemClasses = new TreeMap<>();

	public NearestNeighboursDiTe()
	{

	}

	public synchronized void updatePosition(String ID, String itemClass, double x, double y, boolean updateDuplicates)
	{
		if (this._treeEntries.containsKey(ID) && !updateDuplicates)
		{
			return;
		}

		this.updatePosition(ID, itemClass, x, y);
	}

	public synchronized void updatePosition(String ID, String itemClass, double x, double y)
	{
		if (this._treeEntries.containsKey(ID))
		{
			this._tree = this._tree.delete(this._treeEntries.get(ID));
		}

		Entry<String, Point> newPoint = EntryDefault.entry(ID, Geometries.point(x, y));
		this._treeEntries.put(ID, newPoint);
		this._itemClasses.put(ID, itemClass);

		this._tree = this._tree.add(newPoint);
	}

	public ArrayList<String> getNearestNeighbours(String ID, int n)
	{
		Entry<String, Point> entry = this._treeEntries.get(ID);
		ArrayList<String> nearestNeighbours = new ArrayList<>();

		if (entry != null && entry.geometry() != null)
		{
			// get n+1 nearest neighbours, because the item sees itself as a neighbour
			_tree.nearest(entry.geometry(), Double.POSITIVE_INFINITY, (n + 1)).subscribe(s ->
			{
				if(entry != s && nearestNeighbours.size() < 10)
				{
					nearestNeighbours.add(s.value());
				}
			});
		}

		return nearestNeighbours;
	}

	public TreeMap<String, Integer> getNearestNeighbourClasses(String ID, int n)
	{
		TreeMap<String, Integer> neighbourClasses = new TreeMap<>();
		ArrayList<String> nearestNeighbours = this.getNearestNeighbours(ID, n);

		for (String epc : nearestNeighbours)
		{
			neighbourClasses.putIfAbsent(this._itemClasses.get(epc), 0);
			neighbourClasses.put(this._itemClasses.get(epc), neighbourClasses.get(this._itemClasses.get(epc)) + 1);
		}

		return neighbourClasses;
	}

	public double getNearestNeighbourQuota(String ID, int n)
	{
		TreeMap<String, Integer> neighbourClasses = this.getNearestNeighbourClasses(ID, n);

		int count = neighbourClasses.getOrDefault(this._itemClasses.getOrDefault(ID, ""), 0);

		return (double) count / n;
	}

	public Point getLocation(String ID)
	{
		return this._treeEntries.get(ID).geometry();
	}

	public Set<String> getEPCs()
	{
		return this._treeEntries.keySet();
	}

}
