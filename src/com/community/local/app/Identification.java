/**
 * 
 */
package com.community.local.app;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.community.local.structures.*;
import com.community.local.structures.Node.SetEnum;
import com.community.local.tools.ConfigHelper;
import com.community.local.tools.HandleData;


/**
 * @author user
 *
 */
public class Identification {

	private CommunitySetD D;
	private CommunitySetS S;
	private List<Long> deledNodeNumInS;//S中已删除节点编号
	private long initialNodeNum;

	public static void main(String[] args) {
		
		Date beginDate = new Date();
		long beginTime = beginDate.getTime();
		
		String configFileName= new File("").getAbsolutePath() + "\\config\\config.xml";//"D:\\Study\\Workspaces\\LocalCommunityIdentification\\config\\config.xml";	
		ConfigHelper configHelper = new ConfigHelper(configFileName);
		configHelper.getXMLConfig();
		HandleData.initialData( configHelper.getInputFileName(), 
								configHelper.getSperatorStr(),
								configHelper.getSubsperatorStr(), 
								configHelper.getOutputFileName() );
		
		
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileInputStream = new FileInputStream(configHelper.getInputFileName());
			inputStreamReader = new InputStreamReader(fileInputStream);
			bufferedReader = new BufferedReader(inputStreamReader);	
			try {
				String initialNodeNumString;
				while ( (initialNodeNumString = bufferedReader.readLine()) != null) {
					if (!initialNodeNumString.equals("")){
						
						int endIndex = initialNodeNumString.indexOf(configHelper.getSperatorStr());
						initialNodeNumString = initialNodeNumString.substring(0, endIndex);
						Identification identification = new Identification(Long.parseLong(initialNodeNumString));//Identification(configHelper.getInitialNodeNum());
						System.out.println(initialNodeNumString);
						identification.localCommunityIdentification();
					}
					else {
						break;
					}
				}
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("读取一行数据时出错");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("文件读取路径错误FileNotFoundException");
		} finally{
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		Date endDate = new Date();
		System.out.println(endDate.getTime() - beginTime);
		
	}
	
	public Identification(long initialNodeNum) {
			
		this.initialNodeNum = initialNodeNum;//configHelper.getInitialNodeNum();
		D = new CommunitySetD();
		S = new CommunitySetS();
		deledNodeNumInS = new ArrayList<Long>();//S中已删除节点编号
	}
	
	
	public boolean localCommunityIdentification(){
		Discovery();
		Examination();
		
		
		if (CommunitySet.getNodeByNodeNum(initialNodeNum, D.nodeSet)!=null) {
			HandleData.saveResults(initialNodeNum, D.nodeSet);
			return true;
		}else {
			D.nodeSet.clear();
			HandleData.saveResults(initialNodeNum, D.nodeSet);
			return false;
		}
		
	}
	private double computeNewLin(long nodeNum) {
		double newLin = 0;
		List<Double> IndOutOfNodeI = S.getIndOutdOfNodeI(nodeNum, D.nodeSet);
		
		newLin = (D.getInd()+2*IndOutOfNodeI.get(0)) / (D.nodeSet.size()+1);
		return newLin;
		
	}
	
	private double computeNewLex(long nodeNum) {
		double newLex = 0;
		List<Double> IndOutOfNodeI = S.getIndOutdOfNodeI(nodeNum, D.nodeSet);
		
		newLex = ( D.getOutd()-IndOutOfNodeI.get(0)+IndOutOfNodeI.get(1) ) / D.getSizeOfNewB(nodeNum, S.nodeSet);
		return newLex;
		
	}
	
	/**
	 * updateSetS
	 * 更新S集合
	 * 
	 */
	private boolean updateSetS(long nodeNum){
		
		Node node = CommunitySet.getNodeByNodeNum(nodeNum, D.nodeSet);
		
		Iterator<Entry<Long, Edge>> iteratorEdges = node.getAdjacentEdges().entrySet().iterator();
		while (iteratorEdges.hasNext()) {//遍历一个点的所有邻接边
			Edge edge = iteratorEdges.next().getValue();
			
			Node nodeInAll = CommunitySet.getNodeByNodeNum(edge.getNodeNum(), S.nodeSet);
			if (nodeInAll==null) {
				nodeInAll = CommunitySet.getNodeByNodeNum(edge.getNodeNum(), D.nodeSet);
			}
			if (nodeInAll == null) {//如果在D和S中未找到此点，则生成新点加入S
 				Node newNode = HandleData.generateNode(edge.getNodeNum(), SetEnum.S);
 				if (newNode==null) {
 					System.out.println("updateSetS阶段：邻接点"+edge.getNodeNum()+"不存在，程序退出");
 					return false;
 				}
 				for (Long deledNodeNum : deledNodeNumInS) {
					newNode.delAdjacentEdge(deledNodeNum);
				}
				S.addNode(newNode);
			}
		}
		return true;
		
	}
	private void Discovery() {
		double L;			//初始时为0
		double Lin;
		double Lex;
		double newL;

		Node n0 = HandleData.generateNode(initialNodeNum, SetEnum.D);
		//System.out.println("Discovery阶段开始...");
		if (n0!=null) {
			D.addNode(n0);
			//System.out.println("初始点 "+initialNodeNum+" 加入D");
		}
		else {
			System.out.println("初始点不存在");
			return;
		}
		
		Iterator<Entry<Long, Edge>> iteratorEdges = n0.getAdjacentEdges().entrySet().iterator();
		while (iteratorEdges.hasNext()) {//遍历一个点的所有邻接边
			Edge edge = iteratorEdges.next().getValue();
			Node newNode = HandleData.generateNode(edge.getNodeNum(), SetEnum.S);
			if (newNode==null) {
				System.out.println("邻接点"+edge.getNodeNum()+"不存在，程序退出");
				
				//System.exit(0);
				D.delNode(initialNodeNum, S);
				return;
			}
			S.addNode(newNode);
		}
		
		
		do {
			if ( !D.getIndOutdOfD(S.nodeSet) ){//每次循环 更新D的Ind和Outd
				D.delNode(initialNodeNum, S);
				return;
			}
			
			Lin = D.getInd()/D.nodeSet.size();
			Lex = D.getOutd()/D.getSizeOfB(S.nodeSet);
			L = Lin/Lex;
		
			//计算最大Li'
			newL = 0;		//每轮将newL置0
			double newLin = 0;
			double newLex = 0;
			double maxLI = 0;
			long maxLINodeNum;//初始化最大点编号	
			
			
			if (S.nodeSet.entrySet().iterator().hasNext()) {
				maxLINodeNum = S.nodeSet.entrySet().iterator().next().getValue().getNodeNum();
			}
			else {
				break;
			}
			
			Iterator<Entry<Long, Node>> iteratorSetS = S.nodeSet.entrySet().iterator();
			while (iteratorSetS.hasNext()) {//如果S中没有点了，则newLi不变
				Node nodeInS = iteratorSetS.next().getValue();
				double LinI = 0;
				double LexI = 0;
				double LI = 0;
				//maxLINodeNum = node.getNodeNum();
				
				LinI = computeNewLin(nodeInS.getNodeNum());
				LexI = computeNewLex(nodeInS.getNodeNum());
				LI = LinI/LexI;
				
				if (Double.isNaN(LexI)) {//边界B已为空，则将最后一个点加入D
					newLin = Lin+1;
					newLex = Lex-1;
				}
				
				if ( maxLI < LI) {//如果有最大的Li就更新max以及i对应的Lin，Lex
					maxLI = LI;
					maxLINodeNum = nodeInS.getNodeNum();
					newLin = LinI;
					newLex = LexI;
					newL = LI;
				}
			}
		
			if ( (newLin>Lin && newLex<Lex) || (newLin>Lin && newLex>Lex)) {
				//将maxLINodeNum从S中移除并加入D //setBelongToSet(SetEnum.D);
				Node removeNode = S.removeNode(maxLINodeNum);
				D.addNode(removeNode);
				//System.out.println(maxLINodeNum+"加入D");
				if (!updateSetS(maxLINodeNum)) {
					System.out.println("更新S失败");
					//System.exit(0);
					D.delNode(initialNodeNum, S);
					return;
				}
			}else {
				//将maxLINodeNum从S中删除
				if (S.delNode(maxLINodeNum, D)){
					//System.out.println("从S中删除点"+maxLINodeNum);
					deledNodeNumInS.add(maxLINodeNum);
				}
			}
		} while (newL>L);	//L'>L
		
	}
	
	private void Examination() {	
		//System.out.println("\nExamination阶段开始...");	
		if (CommunitySet.getNodeByNodeNum(initialNodeNum, D.nodeSet)==null) {
			return;
		}
		
		if (D.getSizeOfB(S.nodeSet) == 0) {//边界B已经为空
			//System.out.println("已无边界，完成");
			return;
		}
		Iterator<Entry<Long, Node>> iteratorSetD = D.nodeSet.entrySet().iterator();
		while (iteratorSetD.hasNext()) {//遍历D中的点
			Node nodeInD = iteratorSetD.next().getValue();
			
			double Lin = 0;
			double Lex = 0;
			double newLin = 0;
			double newLex = 0;
			
			
			if (!D.getIndOutdOfD(S.nodeSet) ){//更新D
				D.delNode(initialNodeNum, S);
				return;
			}
			newLin = D.getInd()/D.nodeSet.size();
			newLex = D.getOutd()/D.getSizeOfB(S.nodeSet);
			
			CommunitySetD tempD = new CommunitySetD();
			CommunitySetS tempS = new CommunitySetS();
			tempD.nodeSet.putAll(D.nodeSet);
			tempS.nodeSet.putAll(S.nodeSet);
		
			tempD.delNode(nodeInD.getNodeNum(), tempS);//删除
			if (tempD.getIndOutdOfD(tempS.nodeSet) ){//更新D
				D.delNode(initialNodeNum, S);
				return;
			}
			Lin = tempD.getInd() / tempD.nodeSet.size();
			Lex = tempD.getOutd() / tempD.getSizeOfB(tempS.nodeSet);
			
			if ((newLin>Lin && newLex<Lex)) {
				continue;
			}
			else {
				if (D.delNodeDontRemove(nodeInD.getNodeNum(), S)) {
					iteratorSetD.remove();
					//System.out.println("从D中删除点"+nodeInD.getNodeNum());
					if (nodeInD.getNodeNum() == initialNodeNum) {
						return;
					}
				}		
			}
		}
	}
	public long getInitialNodeNum() {
		return initialNodeNum;
	}
	public void setInitialNodeNum(long initialNodeNum) {
		this.initialNodeNum = initialNodeNum;
	}

}
