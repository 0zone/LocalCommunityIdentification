/**
 * 
 */
package com.community.local.structures;

import java.util.HashMap;

/**
 * @author user
 *
 */
public class Node {
	
	public enum SetEnum {
		D, S
	}
	
	private long nodeNum;				//点编号
	private HashMap<Long, Edge> adjacentEdges = new HashMap<Long, Edge>();		//邻接点集合<邻接点编号, 点编号及权值>
	private SetEnum belongToSet;			//所属集合	//枚举类型
	
	public Node(long nodeNum, HashMap<Long, Edge> adjacentEdges, SetEnum belongToSet) {
		this.setNodeNum(nodeNum);
		this.setAdjacentEdges(adjacentEdges);
		this.setBelongToSet(belongToSet);
	}

	/**
	 * 在邻接点集中增加一个点
	 */
	public void addAdjacentNode(Edge addEdge){
		adjacentEdges.put(addEdge.getNodeNum(), addEdge);
	}
	
	/**
	 * 删边		
	 * 根据点编号删除邻接点集中的某一个点
	 * 删除成功返回true
	 */
	public boolean delAdjacentEdge(long nodeNum){
		adjacentEdges.remove(nodeNum);
		return true;
	}
	
	
	
	public HashMap<Long, Edge> getAdjacentEdges() {
		return adjacentEdges;
	}

	public void setAdjacentEdges(HashMap<Long, Edge> adjacentEdges) {
		this.adjacentEdges = adjacentEdges;
	}

	public long getNodeNum() {
		return nodeNum;
	}

	public void setNodeNum(long nodeNum) {
		this.nodeNum = nodeNum;
	}

	public SetEnum getBelongToSet() {
		return belongToSet;
	}

	public void setBelongToSet(SetEnum belongToSet) {
		this.belongToSet = belongToSet;
	}
}
