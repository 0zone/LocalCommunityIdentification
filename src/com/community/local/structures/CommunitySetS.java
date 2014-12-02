/**
 * 
 */
package com.community.local.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.community.local.structures.Node.SetEnum;

/**
 * @author user
 * ����S
 */
public class CommunitySetS extends CommunitySet {
	
	
	public CommunitySetS() {
		super(SetEnum.S);
	}

	
	
	public List<Double> getIndOutdOfNodeI(long nodeNum, HashMap<Long, Node> setD) {
		double Indi = 0;
		double Outi = 0;
		
		Node node = getNodeByNodeNum(nodeNum, this.nodeSet);
		Iterator<Entry<Long, Edge>> iteratorEdges = node.getAdjacentEdges().entrySet().iterator();
		while (iteratorEdges.hasNext()) {//����һ����������ڽӱ�
			Edge edge = iteratorEdges.next().getValue();
			Node nodeInD = getNodeByNodeNum(edge.getNodeNum(), setD);
			
			if (nodeInD != null) {//�����D���ҵ�����Indi
				Indi = Indi + edge.getEdgeWeight();
			}
			else {
				Outi = Outi + edge.getEdgeWeight();
			}
		}

		List<Double> IndOutOfNode = new ArrayList<Double>();
		IndOutOfNode.add(Indi);
		IndOutOfNode.add(Outi);
		
		return IndOutOfNode;
	}
}
