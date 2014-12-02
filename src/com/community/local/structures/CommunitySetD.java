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
 * Community D
 */
public class CommunitySetD extends CommunitySet {
	private double Ind = 0;
	private double Outd = 0;
	
	public CommunitySetD() {
		super(SetEnum.D);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * ��ȡ�㼯D��Indֵ��Outdֵ
	 * ����Ϊ����S
	 */
	public boolean getIndOutdOfD(HashMap<Long, Node> setS) {
		Ind = 0;
		Outd = 0;
		
		Iterator<Entry<Long, Node>> iteratorSetD = this.nodeSet.entrySet().iterator();
		while (iteratorSetD.hasNext()) {//����D�еĵ�,����Ind��Out
			Node nodeInD = iteratorSetD.next().getValue();
	
			Iterator<Entry<Long, Edge>> iteratorEdges = nodeInD.getAdjacentEdges().entrySet().iterator();
			while (iteratorEdges.hasNext()) {//����һ����������ڽӱ�
				Edge edge = iteratorEdges.next().getValue();
				Node adjNode = getNodeByNodeNum(edge.getNodeNum(), this.nodeSet);
				if (adjNode==null) {
					adjNode = getNodeByNodeNum(edge.getNodeNum(), setS);
				}
				if (adjNode==null) {
					return false;
				}
				switch (adjNode.getBelongToSet()) {//�ж��ڽӵ�������һ�㼯
					case D:
						Ind = Ind + edge.getEdgeWeight();
						break;
					case S:
						Outd = Outd + edge.getEdgeWeight();
						break;
					default:
						break;
				}
			}
		}
		return true;
	}
	public int getSizeOfB(HashMap<Long, Node> setS) {
		int sizeOfB = 0;
		
		Iterator<Entry<Long, Node>> iteratorSetD = this.nodeSet.entrySet().iterator();
		while (iteratorSetD.hasNext()) {//����D�еĵ�
			Node nodeInD = iteratorSetD.next().getValue();
			
			Iterator<Entry<Long, Edge>> iteratorEdges = nodeInD.getAdjacentEdges().entrySet().iterator();
			while (iteratorEdges.hasNext()) {//�����ڽӵ�
				Edge edge = iteratorEdges.next().getValue();
				Node adjNode = getNodeByNodeNum(edge.getNodeNum(), this.nodeSet);
				
				if (adjNode==null) {
					sizeOfB++;//����ڽӵ㲻����D����˵�����S��nodeInD����B
					break;
				}
			}
		
		}
		return sizeOfB;
		
	}
	/**
	 * ��ȡ���ڵ�i����D�к���±߽�B�Ĵ�С
	 * ����Ϊ��Ҫ�¼���D�ĵ��ţ�����S
	 */
	public int getSizeOfNewB(long nodeNum, HashMap<Long, Node> setS){
		int sizeOfNewB = 0;
		
		HashMap<Long, Node> tempSetS = new HashMap<Long, Node>();
		HashMap<Long, Node> tempSetD = new HashMap<Long, Node>();
		
		tempSetS.putAll(setS);
		tempSetD.putAll(this.nodeSet);
		
		Node newNode = getNodeByNodeNum(nodeNum, tempSetS);
		
		tempSetS.remove(newNode.getNodeNum());
		tempSetD.put(newNode.getNodeNum(), newNode);

		
		Iterator<Entry<Long, Node>> iteratortempSetD = tempSetD.entrySet().iterator();
		while (iteratortempSetD.hasNext()) {//����D�еĵ�
			Node nodeInTempD = iteratortempSetD.next().getValue();
			
			Iterator<Entry<Long, Edge>> iteratorEdges = nodeInTempD.getAdjacentEdges().entrySet().iterator();
			while (iteratorEdges.hasNext()) {//�����ڽӵ�
				Edge edge = iteratorEdges.next().getValue();
				
				Node adjNode = getNodeByNodeNum(edge.getNodeNum(), tempSetD);
				
				if (adjNode==null) {//����ڽӵ㲻����D����˵�����S��nodeInTempD����B
					sizeOfNewB++;
					break;
				}
			}
		}
		return sizeOfNewB;
	}
	
	/**
	 * �ҵ�Ҫɾ���ĵ㣬��ȡ��˵��ڽӵĵ㼯������ɾ��
	 * �������õ��Ƴ�����
	 * ɾ���ɹ�����true
	 */
	
	public boolean delNodeDontRemove(long delNodeNum, CommunitySet anotherSet) {
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
			}	
			return true;
		}		
		return false;
	}

	public double getInd() {
		return Ind;
	}

	public void setInd(double ind) {
		Ind = ind;
	}

	public double getOutd() {
		return Outd;
	}

	public void setOutd(double outd) {
		Outd = outd;
	}


}
