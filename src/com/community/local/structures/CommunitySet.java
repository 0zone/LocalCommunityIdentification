/**
 * 
 */
package com.community.local.structures;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import com.community.local.structures.Node.SetEnum;

/**
 * @author user
 *
 */
public class CommunitySet {
	public final SetEnum setName;
	public HashMap<Long, Node> nodeSet = new  HashMap<Long, Node>();		//点集
	
	public CommunitySet(SetEnum setName) {
		this.setName = setName;
	}
	/**
	 * 根据点编号获取点集中的某一个点
	 * 返回点
	 */
	public static Node getNodeByNodeNum(long nodeNum, HashMap<Long, Node> nodeSet) {
		
		return nodeSet.get(nodeNum);
	}
	
	public void addNode(Node addNode) {
		addNode.setBelongToSet(this.setName);
		nodeSet.put(addNode.getNodeNum(), addNode);
	}
	public Node removeNode(long removeNodeNum) {
		return this.nodeSet.remove(removeNodeNum);
	}
	/**
	 * 找到要删除的点，获取与此点邻接的点集，删除
	 * 根据点编号删除点集中的某一个点
	 * 删除成功返回true
	 */
	public boolean delNode(long delNodeNum, CommunitySet anotherSet) {
		Node delNode = getNodeByNodeNum(delNodeNum, this.nodeSet);
		
		if (delNode != null) {		

			Iterator<Entry<Long, Edge>> iterator = delNode.getAdjacentEdges().entrySet().iterator();
			
			while (iterator.hasNext()) {//遍历将要删除点的邻接边集
				Edge edge = iterator.next().getValue();//获取被删点的邻接边
				
				Node adjNodeOfDelNode = getNodeByNodeNum(edge.getNodeNum(), this.nodeSet);//邻接点
				if (adjNodeOfDelNode == null) {
					adjNodeOfDelNode = getNodeByNodeNum(edge.getNodeNum(), anotherSet.nodeSet);
				}	
				if (adjNodeOfDelNode != null) {
					//从邻接点中把与要删除点相连的边删除
					adjNodeOfDelNode.delAdjacentEdge(delNodeNum);
				}
				else {//如果邻接点不在集合D和S中，则此点还未加入
					//Identification类维护一个已删节点列表，加入新点时遍历已删点列表
				}
				
			}
			
			this.removeNode(delNodeNum);//从点集中删除指定点
			return true;
		}		
		return false;
	}
	
}
