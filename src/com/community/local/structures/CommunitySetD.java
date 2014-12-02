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
	 * 获取点集D的Ind值和Outd值
	 * 参数为集合S
	 */
	public boolean getIndOutdOfD(HashMap<Long, Node> setS) {
		Ind = 0;
		Outd = 0;
		
		Iterator<Entry<Long, Node>> iteratorSetD = this.nodeSet.entrySet().iterator();
		while (iteratorSetD.hasNext()) {//遍历D中的点,计算Ind和Out
			Node nodeInD = iteratorSetD.next().getValue();
	
			Iterator<Entry<Long, Edge>> iteratorEdges = nodeInD.getAdjacentEdges().entrySet().iterator();
			while (iteratorEdges.hasNext()) {//遍历一个点的所有邻接边
				Edge edge = iteratorEdges.next().getValue();
				Node adjNode = getNodeByNodeNum(edge.getNodeNum(), this.nodeSet);
				if (adjNode==null) {
					adjNode = getNodeByNodeNum(edge.getNodeNum(), setS);
				}
				if (adjNode==null) {
					return false;
				}
				switch (adjNode.getBelongToSet()) {//判断邻接点属于哪一点集
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
		while (iteratorSetD.hasNext()) {//遍历D中的点
			Node nodeInD = iteratorSetD.next().getValue();
			
			Iterator<Entry<Long, Edge>> iteratorEdges = nodeInD.getAdjacentEdges().entrySet().iterator();
			while (iteratorEdges.hasNext()) {//遍历邻接点
				Edge edge = iteratorEdges.next().getValue();
				Node adjNode = getNodeByNodeNum(edge.getNodeNum(), this.nodeSet);
				
				if (adjNode==null) {
					sizeOfB++;//如果邻接点不属于D，则此点属于S，nodeInD属于B
					break;
				}
			}
		
		}
		return sizeOfB;
		
	}
	/**
	 * 获取将节点i加入D中后的新边界B的大小
	 * 参数为将要新加入D的点编号，集合S
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
		while (iteratortempSetD.hasNext()) {//遍历D中的点
			Node nodeInTempD = iteratortempSetD.next().getValue();
			
			Iterator<Entry<Long, Edge>> iteratorEdges = nodeInTempD.getAdjacentEdges().entrySet().iterator();
			while (iteratorEdges.hasNext()) {//遍历邻接点
				Edge edge = iteratorEdges.next().getValue();
				
				Node adjNode = getNodeByNodeNum(edge.getNodeNum(), tempSetD);
				
				if (adjNode==null) {//如果邻接点不属于D，则此点属于S，nodeInTempD属于B
					sizeOfNewB++;
					break;
				}
			}
		}
		return sizeOfNewB;
	}
	
	/**
	 * 找到要删除的点，获取与此点邻接的点集，将边删除
	 * 但不将该点移除集合
	 * 删除成功返回true
	 */
	
	public boolean delNodeDontRemove(long delNodeNum, CommunitySet anotherSet) {
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
