package roke.Datamining;

import java.util.ArrayList;
import java.util.Scanner;

import roke.tool.DecimalCalculate;

public class Main {
	public static void main(String[] args){
		ArrayList<ArrayList<String>> trainList ;
		ArrayList<ArrayList<String>> testList ;
		
		String originalTrain = "file/adult.data";
		String processedTrain = "process/adultResult.txt";
		String originalTest = "file/adult1000.test";
		String originalTest100 = "file/adult100.test";
		String processedTest = "process/adult1000Result.txt";
		String processedTest100 = "process/adult100Result.txt";
		
		 String finalStr = "";
		 int wrong_number = 0; //????????????
		 double finalData = 0.0; //??????
		 
		 boolean type = false; //????????
		 boolean flag = false; //????????????
		 for(int i = 0;i < 2;i++){
			 if(i == 0){
//				 Scanner scanner = new Scanner(System.in);
//				 String str = scanner.next();
				 flag = true;
//				 if(str.equals("y")){
//					 flag = true;
//				 }else if(str.equals("n")){
//					 flag = false;
//				 }else{
//					 i = -1;
//				 }
			 }else{

//				 Scanner scanner = new Scanner(System.in);
//				 String str = scanner.next();
//				 if(str.equals("y")){ //��ȥ����
//					 type = true;
//				 }else if(str.equals("n")){
//					 type = false;
//				 }else{
//					 i = 0;
//				 }
				 type = true;
			 }
		 }
		PreRead convert = new PreRead();
		convert.readFile(originalTrain, processedTrain,type); //???????
		if(!flag){
			convert.readFile(originalTest, processedTest,type); //1000????????
			testList = convert.readTest(processedTest);
		}else{
			convert.readFile(originalTest100, processedTest100,type); //100??????????
			testList = convert.readTest(processedTest100);
		}
		
		trainList =convert.readTest(processedTrain);	
		Bayes bayes = new Bayes();
		for(int i = 0;i < testList.size();i++){
			ArrayList<String> tmp;
			tmp = testList.get(i);
			String label = tmp.get(tmp.size()-1);
			tmp.remove(tmp.size() - 1);
			finalStr = bayes.predictClass(trainList, tmp);
			
			if(!label.equals(finalStr)){
				wrong_number ++;
			}
		}
		//finalData = DecimalCalculate.div(count, testList.size(),3); 
		System.out.println((DecimalCalculate.sub(1.00000000, DecimalCalculate.div(wrong_number, testList.size())))*100 + "%");
	}
}
