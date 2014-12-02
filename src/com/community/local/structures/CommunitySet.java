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
	public HashMap<Long, Node> nodeSet = new  HashMap<Long, Node>();		//�㼯
	
	public CommunitySet(SetEnum setName) {
		this.setName = setName;
	}
	/**
	 * ���ݵ��Ż�ȡ�㼯�е�ĳһ����
	 * ���ص�
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
	 * �ҵ�Ҫɾ���ĵ㣬��ȡ��˵��ڽӵĵ㼯��ɾ��
	 * ���ݵ���ɾ���㼯�е�ĳһ����
	 * ɾ���ɹ�����true
	 */
	public boolean delNode(long delNodeNum, CommunitySet anotherSet) {
		Node delNode = getNodeByNodeNum(delNodeNum, this.nodeSet);
		
		if (delNode != null) {		

			Iterator<Entry<Long, Edge>> iterator = delNode.getAdjacentEdges().entrySet().iterator();
			
			while (iterator.hasNext()) {//������Ҫɾ������ڽӱ߼�
				Edge edge = iterator.next().getValue();//��ȡ��ɾ����ڽӱ�
				
				Node adjNodeOfDelNode = getNodeByNodeNum(edge.getNodeNum(), this.nodeSet);//�ڽӵ�
				if (adjNodeOfDelNode == null) {
					adjNodeOfDelNode = getNodeByNodeNum(edge.getNodeNum(), anotherSet.nodeSet);
				}	
				if (adjNodeOfDelNode != null) {
					//���ڽӵ��а���Ҫɾ���������ı�ɾ��
					adjNodeOfDelNode.delAdjacentEdge(delNodeNum);
				}
				else {//����ڽӵ㲻�ڼ���D��S�У���˵㻹δ����
					//Identification��ά��һ����ɾ�ڵ��б������µ�ʱ������ɾ���б�
				}
				
			}
			
			this.removeNode(delNodeNum);//�ӵ㼯��ɾ��ָ����
			return true;
		}		
		return false;
	}
	
}
