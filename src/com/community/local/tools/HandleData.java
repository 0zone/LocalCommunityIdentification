/**
 * 
 */
package com.community.local.tools;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.community.local.structures.Edge;
import com.community.local.structures.Node;
import com.community.local.structures.Node.SetEnum;

/**
 * @author user
 *
 */
public class HandleData {
	
	private static String speratorStr;		//数据分隔符
	private static String subsperatorStr;	//数据子分隔符	从xml中获取
	private static String inputFileName;	//数据路径
	private static String outputFileName;
/*	private static */
	private static String allNodesInfoStr;
	
	public static void initialData(String inputFileName, String speratorStr, String subsperatorStr, String outputFileName) {
		setInputFileName(inputFileName);
		setSperatorStr(speratorStr);
		setSubsperatorStr(subsperatorStr);
		setOutputFileName(outputFileName);
		
		
		FileInputStream fileInputStream = null;
		FileChannel fileChannel = null;
		MappedByteBuffer mappedInputFileByteBuffer;
		try {	
			fileInputStream = new FileInputStream(inputFileName);
			fileChannel = fileInputStream.getChannel();
			long inputFileSize = fileChannel.size();
			mappedInputFileByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputFileSize);
			
			Charset charset = Charset.forName("UTF8");
			CharsetDecoder decoder = charset.newDecoder();
			allNodesInfoStr = "\n"+decoder.decode(mappedInputFileByteBuffer).toString();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (fileChannel != null) {
				try {
					fileChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static boolean saveResults(long initialNodeNum, HashMap<Long, Node> resultNodeSet) {
		String resultStr = String.valueOf(initialNodeNum);
		resultStr = resultStr + speratorStr;

		Iterator<Entry<Long, Node>> iteratorSetD = resultNodeSet.entrySet().iterator();
		while (iteratorSetD.hasNext()) {//遍历D中的点
			Node nodeInD = iteratorSetD.next().getValue();
			if (nodeInD.getNodeNum() == initialNodeNum) {
				continue;
			}
			resultStr = resultStr + nodeInD.getNodeNum() + subsperatorStr;
			if ("".equals(subsperatorStr)) {
				resultStr = resultStr + " ";
			}
		}
		if (resultStr.endsWith(subsperatorStr)) {
			resultStr = resultStr.substring(0, resultStr.length()-1);
		}
		
		if(resultStr.endsWith(speratorStr)){
			resultStr = resultStr + "false";
		}
		File outputFile = new File(outputFileName);  
		
		try {  
			BufferedWriter writer  = new BufferedWriter(new FileWriter(outputFile, true));  
			
			writer.write(resultStr+"\n");  
			writer.flush();  
			writer.close();  
		} catch (FileNotFoundException e) {  
		    e.printStackTrace();  
		} catch (IOException e) {  
		    e.printStackTrace();  
		} 
		
		return true;
		
	}
	
	/**
	 * 根据节点编号获取节点信息
	 * 生成新节点返回
	 */
	public static Node generateNode(long nodeNum, SetEnum belongToSet) {

		String nodeInfoStr = getNodeInfoStr(nodeNum);//从文件读取指定点信息
		
		if (nodeInfoStr == null) {
			return null;
		}
		String[] adjEdges = nodeInfoStr.split(speratorStr);
		HashMap<Long, Edge> adjacentEdges = new HashMap<Long, Edge>();
		for (String edgeStr : adjEdges) {	//生成点的邻接边集
			long adjNodeNum;
			double adjNodeWeight;
			
			if("".equals(subsperatorStr)){
				adjNodeNum = Long.parseLong(edgeStr);
				adjNodeWeight = 1;
			}else {
				String[] edgeInfo = edgeStr.split(subsperatorStr);
				adjNodeNum = Long.parseLong(edgeInfo[0]);
				adjNodeWeight = Double.parseDouble(edgeInfo[1]);
			}
			
			
			Edge adjEdge = new Edge();
			adjEdge.setNodeNum(adjNodeNum);
			adjEdge.setEdgeWeight(adjNodeWeight);
			
			adjacentEdges.put(adjEdge.getNodeNum(), adjEdge);	
		}
		Node node = new Node(nodeNum, adjacentEdges, belongToSet);
		
		return node;
		
	}
	
	/**
	 * 读取数据文件，根据节点编号获取节点信息
	 */
	private static String getNodeInfoStr(long nodeNum) {
		String nodeInfoStr = null;//节点信息字符串
		String nodeLocationStr = "\n" + nodeNum + speratorStr;
					
		int nodeBegin = allNodesInfoStr.indexOf(nodeLocationStr);
		int nodeEnd = allNodesInfoStr.indexOf("\n", nodeBegin+1);
		if(nodeBegin > -1)
		{
			nodeInfoStr = allNodesInfoStr.substring(nodeBegin+nodeLocationStr.length(), nodeEnd);
		}

		if ("".equals(nodeInfoStr)) {
			nodeInfoStr=null;
		}
		return nodeInfoStr;
	}


	public static String getSperatorStr() {
		return speratorStr;
	}

	public static void setSperatorStr(String speratorStr) {
		HandleData.speratorStr = speratorStr;
	}

	public static String getSubsperatorStr() {
		return subsperatorStr;
	}

	public static void setSubsperatorStr(String subsperatorStr) {
		HandleData.subsperatorStr = subsperatorStr;
	}
	public static String getInputFileName() {
		return inputFileName;
	}
	public static void setInputFileName(String inputFileName) {
		HandleData.inputFileName = inputFileName;
	}
	public static String getOutputFileName() {
		return outputFileName;
	}
	public static void setOutputFileName(String outputFileName) {
		HandleData.outputFileName = outputFileName;
	}


}
