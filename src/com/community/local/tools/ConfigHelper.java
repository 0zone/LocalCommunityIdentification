/**
 * 
 */
package com.community.local.tools;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author user
 *
 */
public class ConfigHelper {
	private String configFileName;
	
	private long initialNodeNum;
	private String inputFileName;
	private String outputFileName;
	private String speratorStr;
	private String subsperatorStr;
	
	public ConfigHelper(String configFileName) {
		setConfigFileName(configFileName);
	}
	public void getXMLConfig() {
		
		
		try {
			File configFile = new File(configFileName);    
			SAXReader reader = new SAXReader();    
			Document configdDocument = reader.read(configFile);
			Element configElement = configdDocument.getRootElement();   
			
			initialNodeNum = Long.parseLong(configElement.elementText("initial_node_num"));
			inputFileName = configElement.elementText("input_file_name");
			outputFileName = configElement.elementText("output_file_name");
			speratorStr = configElement.elementText("sperator_str");
			subsperatorStr = configElement.elementText("subsperator_str");

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			
			System.out.println("未找到配置文件，检查配置文件路径");
			System.exit(0);
			
		} finally {
			
		}
   
	}
	
	public String getSperatorStr() {
		return speratorStr;
	}
	public void setSperatorStr(String speratorStr) {
		this.speratorStr = speratorStr;
	}
	public String getSubsperatorStr() {
		return subsperatorStr;
	}
	public void setSubsperatorStr(String subsperatorStr) {
		this.subsperatorStr = subsperatorStr;
	}

	public long getInitialNodeNum() {
		return initialNodeNum;
	}


	public void setInitialNodeNum(long initialNodeNum) {
		this.initialNodeNum = initialNodeNum;
	}


	public String getInputFileName() {
		return inputFileName;
	}


	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}


	public String getOutputFileName() {
		return outputFileName;
	}


	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	public String getConfigFileName() {
		return configFileName;
	}
	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}
	
	
}
