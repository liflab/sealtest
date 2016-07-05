/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.ecp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Node in a spanning tree, as used in the union find algorithm for
 * computing the minimum spanning tree of a graph.
 * <p>
 * NOTE: the code for the spanning tree has been adapted to Java from
 * <a href="http://cafim.sssup.it/~giulio/software/spanntree/spanning_tree.html">Giulio Bottazzi</a>'s
 * C implementation.
 *
 * @param <T> The type of event of the underlying vertices of the
 *   Cayley graph
 */
public class ForestNode<T extends Event> 
{
	/**
	 * A value of "rank"
	 */
	protected int m_rank;

	/**
	 * The parent of this vertex, or <tt>null</tt> if this vertex is
	 * a root of the spanning forest 
	 */
	protected ForestNode<T> m_parent;

	/**
	 * The actual Cayley Graph vertex this instance is wrapping
	 */
	protected Vertex<T> m_value;

	/**
	 * Creates a new forest node from a graph vertex
	 * @param vertex The vertex
	 */
	public ForestNode(Vertex<T> vertex)
	{
		super();
		m_rank = 0;
		m_parent = null;
		m_value = vertex;
	}

	/**
	 * Gets the rank of this node
	 * @return The rank
	 */
	public int getRank()
	{
		return m_rank;
	}

	/**
	 * Sets the rank of this node
	 * @param rank The rank
	 */
	public void setRank(int rank)
	{
		m_rank = rank;
	}

	/**
	 * Gets the parent of this node
	 * @return The parent, or <tt>null</tt> if this node is the
	 *   root of the tree
	 */
	public ForestNode<T> getParent()
	{
		return m_parent;
	}

	/**
	 * Sets the parent of this node
	 * @param parent The parent
	 */
	public void setParent(ForestNode<T> parent)
	{
		m_parent = parent;
	}
	
	/**
	 * Gets the value of this node, i.e. the underlying vertex
	 * @return The value
	 */
	public Vertex<T> getValue()
	{
		return m_value;
	}

	public ForestNode<T> find(ForestNode<T> node)
	{
		ForestNode<T> temp;
		// Find the root
		ForestNode<T> root = node;
		while (node.getParent() != null) 
		{
			root = node.getParent();
			node = node.getParent();
		}
		// Update the parent pointers
		while (node.getParent() != null) 
		{
			temp = node.getParent();
			node.setParent(root);
			node = temp;
		}
		return root;
	}

	public void union(ForestNode<T> node1, ForestNode<T> node2) 
	{
		// Modified according to Wikipedia
		ForestNode<T> root1 = find(node1); 
		ForestNode<T> root2 = find(node2); 
		if (root1.getRank() > root2.getRank())
		{ 
			root2.setParent(root1); 
		} 
		else if (root2.getRank() > root1.getRank()) 
		{ 
			root1.setParent(root2); 
		} 
		else 
		{ 
			// They are equal 
			root2.setParent(root1); 
			root1.setRank(root1.getRank() + 1); 
		}
	}

	/** 
	 * Compute an ordered spanning tree of a weighted graph. If the graph
	 * is not connected, a minimum spanning forest is returned.
	 *
	 * @param graph The Cayley graph
	 * @return list of edges in the spanning tree
	 */
	public List<Edge<T>> getOrderedSpanningTree(CayleyGraph<T,?> graph)
	{
		List<Edge<T>> tree = new ArrayList<Edge<T>>();
		List<Edge<T>> edges = graph.getEdges();

		// Sort the list of edges by their weight
		/*
		 * We comment this out; in our case, all edges have the same weight
		 * (=1), so we don't need to sort them. If edges had different weights,
		 * they would need to be sorted in order of increasing weight (for
		 * minimum tree) or decreasing weight (for maximum tree) --SH
		 * Collections.sort(edges);
		 */

		// Create the forest
		List<ForestNode<T>> forest = new ArrayList<ForestNode<T>>();
		for (Vertex<T> vertex : graph.getVertices())
		{
			forest.add(new ForestNode<T>(vertex));
		}
		
		// Main loop
		for (Edge<T> edge : edges)
		{
			int v1 = edge.getSource();
			int v2 = edge.getDestination();
			ForestNode<T> fv1 = getNodeWithId(v1, forest);
			ForestNode<T> fv2 = getNodeWithId(v2, forest);
			if (find(fv1) != find(fv2))
			{
				tree.add(edge);
				union(fv1, fv2);
			}
		}
		return tree;
	}
	
	protected ForestNode<T> getNodeWithId(int id, Collection<ForestNode<T>> nodes)
	{
		for (ForestNode<T> node : nodes)
		{
			if (node.getValue().getId() == id)
			{
				return node;
			}
		}
		return null;
	}
}
