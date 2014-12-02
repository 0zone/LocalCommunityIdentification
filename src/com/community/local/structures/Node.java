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
	
	private long nodeNum;				//����
	private HashMap<Long, Edge> adjacentEdges = new HashMap<Long, Edge>();		//�ڽӵ㼯��<�ڽӵ���, ���ż�Ȩֵ>
	private SetEnum belongToSet;			//��������	//ö������
	
	public Node(long nodeNum, HashMap<Long, Edge> adjacentEdges, SetEnum belongToSet) {
		this.setNodeNum(nodeNum);
		this.setAdjacentEdges(adjacentEdges);
		this.setBelongToSet(belongToSet);
	}

	/**
	 * ���ڽӵ㼯������һ����
	 */
	public void addAdjacentNode(Edge addEdge){
		adjacentEdges.put(addEdge.getNodeNum(), addEdge);
	}
	
	/**
	 * ɾ��		
	 * ���ݵ���ɾ���ڽӵ㼯�е�ĳһ����
	 * ɾ���ɹ�����true
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
