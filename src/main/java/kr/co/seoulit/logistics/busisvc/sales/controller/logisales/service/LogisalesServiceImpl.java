package kr.co.seoulit.logistics.busisvc.sales.controller.logisales.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.mapper.ContractMapper;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.mapper.EstimateMapper;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.ContractDetailTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.ContractInfoTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.EstimateDetailTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.EstimateTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class LogisalesServiceImpl implements LogisalesService {
	
	@Autowired
	private ContractMapper contractMapper;
	@Autowired
	private EstimateMapper estimateMapper;


	@Override
	public ArrayList<EstimateTO> getEstimateList(String dateSearchCondition, String startDate, String endDate) {

		ArrayList<EstimateTO> estimateTOList = null;
		
		HashMap<String, String> map = new HashMap<>();
		
		map.put("dateSearchCondition", dateSearchCondition);
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		estimateTOList = estimateMapper.selectEstimateList(map);

		return estimateTOList;
	}

	@Override
	public ArrayList<EstimateDetailTO> getEstimateDetailList(String estimateNo) {

		ArrayList<EstimateDetailTO> estimateDetailTOList = null;

		estimateDetailTOList = estimateMapper.selectEstimateDetailList(estimateNo);

		return estimateDetailTOList;
	}

	@Override
	public ModelMap addNewEstimate(String estimateDate, EstimateTO newEstimateTO) {
		ModelMap resultMap = null;
		String newEstimateNo = getNewEstimateNo(estimateDate);
		// 밑에있는 메서드(getNewEstimateNo)를 불러옴 estimateNo값을 들고옴
		newEstimateTO.setEstimateNo(newEstimateNo);
		//EstimateTO(effectiveDate=2022-11-16, estimateNo=null, estimateRequester=강자훈, description=, contractStatus=null, customerCode=PTN-14, personCodeInCharge=EMP-00, personNameCharge=강자훈, estimateDate=2022-11-08, customerName=gege, estimateDetailTOList=[EstimateDetailTO(unitOfEstimate=EA, estimateNo=null, unitPriceOfEstimate=75000, estimateDetailNo=null, sumPriceOfEstimate=525000, description=, itemCode=DK-AP01, estimateAmount=7, dueDateOfEstimate=2022-11-18, itemName=카메라 본체 NO.1)])위에거
		// newEstimateTO.setEstimateNo(newEstimateNo) 하면 ES2022110858 이 들어감
		System.out.println(newEstimateTO+"밑에");
		estimateMapper.insertEstimate(newEstimateTO);
		ArrayList<EstimateDetailTO> estimateDetailTOList = newEstimateTO.getEstimateDetailTOList(); //bean객체
		System.out.println(estimateDetailTOList+"array!#!#");
		//[EstimateDetailTO(unitOfEstimate=EA, estimateNo=null, unitPriceOfEstimate=71000, estimateDetailNo=null, sumPriceOfEstimate=497000, description=, itemCode=DK-AP02, estimateAmount=7, dueDateOfEstimate=2022-11-18, itemName=카메라 본체 NO.2)]
		for (EstimateDetailTO bean : estimateDetailTOList) {
				// bean은 위와 같지만 배열[]을 제외하고 for문 돌림
			String newEstimateDetailNo = getNewEstimateDetailNo(newEstimateNo);
			//System.out.println(newEstimateDetailNo+"포문안에 넘버"); //ESES2022110864-01
			bean.setEstimateNo(newEstimateNo);
			//System.out.println(bean+"1번째 빈");
			//EstimateDetailTO(unitOfEstimate=EA, estimateNo=ES2022110864, unitPriceOfEstimate=75000, estimateDetailNo=null, sumPriceOfEstimate=33375000, description=, itemCode=DK-AP01, estimateAmount=445, dueDateOfEstimate=2022-11-17, itemName=카메라 본체 NO.1)
			bean.setEstimateDetailNo(newEstimateDetailNo);
			//System.out.println(bean+"2번째 빈"); // 위와 똑같지만 estimateDetailNo=ESES2022110864-01만 추가
		}
		resultMap = batchEstimateDetailListProcess(estimateDetailTOList,newEstimateNo);
	  	//ES2022110857,{INSERT=[ESES2022110857-01], UPDATE=[], DELETE=[], newEstimateNo=ES2022110857}
		resultMap.put("newEstimateNo", newEstimateNo);
		//System.out.println(newEstimateNo+"lllll"+resultMap);
		return resultMap;
	}

	public String getNewEstimateNo(String estimateDate) {

		StringBuffer newEstimateNo = null;
		System.out.println(newEstimateNo+"");
		int i = estimateMapper.selectEstimateCount(estimateDate);
		//System.out.println(i+"ii"); //65

		newEstimateNo = new StringBuffer();
		//System.out.println(newEstimateNo+"11");  // 아무것도 안나옴

		newEstimateNo.append("ES");
		//System.out.println(newEstimateNo+"22");  //ES

		newEstimateNo.append(estimateDate.replace("-", ""));
		//System.out.println(newEstimateNo+"33"); //ES20221108

		newEstimateNo.append(String.format("%02d", i));
		//System.out.println(newEstimateNo+"44"); //ES2022110865

		return newEstimateNo.toString();
	}
	
	public String getNewEstimateDetailNo(String estimateNo) {

		StringBuffer newEstimateDetailNo = null;

		int i = estimateMapper.selectEstimateDetailSeq(estimateNo);

		newEstimateDetailNo = new StringBuffer();
		newEstimateDetailNo.append("ES");
		newEstimateDetailNo.append(estimateNo.replace("-", ""));
		newEstimateDetailNo.append("-"); 
		newEstimateDetailNo.append(String.format("%02d", i));		   

		return newEstimateDetailNo.toString();
	}

	@Override
	public ModelMap removeEstimate(String estimateNo, String status) {

		ModelMap resultMap = null;

		estimateMapper.deleteEstimate(estimateNo);
			
		ArrayList<EstimateDetailTO> estimateDetailTOList = getEstimateDetailList(estimateNo);
			
		for (EstimateDetailTO bean : estimateDetailTOList) {
				
			bean.setStatus(status);
				
		}
			
		resultMap = batchEstimateDetailListProcess(estimateDetailTOList,estimateNo);

		resultMap.put("removeEstimateNo", estimateNo);

		return resultMap;
	}

	@Override
	public ModelMap batchEstimateDetailListProcess(ArrayList<EstimateDetailTO> estimateDetailTOList,String estimateNo) {
		
		ModelMap resultMap = new ModelMap();
		
		ArrayList<EstimateDetailTO> list = new ArrayList<>();
		
		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();

		estimateMapper.initDetailSeq();
		list = estimateMapper.selectEstimateDetailCount(estimateNo);
		TreeSet<Integer> intSet = new TreeSet<>();
		int cnt;

		for(EstimateDetailTO bean : list) {
			String estimateDetailNo = bean.getEstimateDetailNo();
			int no = Integer.parseInt(estimateDetailNo.split("-")[1]);
			intSet.add(no);
		}

		if (intSet.isEmpty()) {
			cnt =  1;
		} else {
			cnt =  intSet.pollLast() + 1;
		}
		
		boolean isDelete=false;
		for (EstimateDetailTO bean : estimateDetailTOList) {

			String status = bean.getStatus();

			switch (status) {

			case "INSERT":
				if(cnt==1) {
					estimateMapper.insertEstimateDetail(bean);
				}else {
					ArrayList<EstimateDetailTO> newList = estimateMapper.selectEstimateDetailCount(estimateNo);
					int newCnt;
					for(EstimateDetailTO newbean : newList) {
						String estimateDetailNo = newbean.getEstimateDetailNo();
						int no = Integer.parseInt(estimateDetailNo.split("-")[1]);
						intSet.add(no);
					}

					if (intSet.isEmpty()) {
						newCnt =  1;
					} else {
						newCnt =  intSet.pollLast() + 1;
					}
					StringBuffer newEstimateDetailNo = new StringBuffer();
					newEstimateDetailNo.append("ES");
					newEstimateDetailNo.append(estimateNo.replace("-", ""));
					newEstimateDetailNo.append("-"); 
					newEstimateDetailNo.append(String.format("%02d", newCnt));	
					bean.setEstimateDetailNo(newEstimateDetailNo.toString());
					estimateMapper.insertEstimateDetail(bean);
				}
				insertList.add(bean.getEstimateDetailNo());
				break;
					
			case "UPDATE":
				estimateMapper.updateEstimateDetail(bean);
				updateList.add(bean.getEstimateDetailNo());
				break;
					
			case "DELETE":
				estimateMapper.deleteEstimateDetail(bean);
				deleteList.add(bean.getEstimateDetailNo());
				isDelete=true;
				//기존의 값을 삭제했을 경우
				break;
			}
		}
		if(isDelete==true) {
			for (EstimateDetailTO bean : estimateDetailTOList) {
				int i = estimateMapper.selectEstimateDetailSeq(estimateNo);
				String newSeq = String.format("%02d", i);
				HashMap<String, String> map=new HashMap<>();
				map.put("estimateDetailNo", bean.getEstimateDetailNo());
				map.put("newSeq", newSeq);
				estimateMapper.reArrangeEstimateDetail(map);
			}
			estimateMapper.initDetailSeq();
		}
		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public ArrayList<ContractInfoTO> getContractList(String searchCondition, String startDate, String endDate, String customerCode) {
		ArrayList<ContractInfoTO> contractInfoTOList = null;
		
		HashMap<String, String> map = new HashMap<>();

		map.put("searchCondition", searchCondition);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("customerCode", customerCode);

		contractInfoTOList = contractMapper.selectContractList(map);
		
		for (ContractInfoTO bean : contractInfoTOList) {
			bean.setContractDetailTOList(contractMapper.selectContractDetailList(bean.getContractNo()));
		}
		return contractInfoTOList;
	}

	
	@Override
	public ArrayList<ContractDetailTO> getContractDetailList(String contractNo) {

		ArrayList<ContractDetailTO> contractDetailTOList = null;

		contractDetailTOList = contractMapper.selectContractDetailList(contractNo);

		return contractDetailTOList;
	}

	@Override
	public ArrayList<EstimateTO> getEstimateListInContractAvailable(String startDate, String endDate) {

		ArrayList<EstimateTO> estimateListInContractAvailable = null;
		
		HashMap<String, String> map = new HashMap<>();

		map.put("startDate", startDate);
		map.put("endDate", endDate);

		estimateListInContractAvailable = contractMapper.selectEstimateListInContractAvailable(map);

		for (EstimateTO bean : estimateListInContractAvailable) {

			bean.setEstimateDetailTOList(estimateMapper.selectEstimateDetailList(bean.getEstimateNo()));//ES2022011360

		}

		return estimateListInContractAvailable;
	}

	@Override
	public ModelMap addNewContract(HashMap<String,String[]>  workingContractList) {

		ModelMap resultMap = new ModelMap();
		HashMap<String,String> setValue = null;
		StringBuffer str = null;

		setValue=new HashMap<String,String>();
		for(String key:workingContractList.keySet()) {
			str=new StringBuffer();

			// {수주상세번호,수주유형,수주등록자...)
			for(String value:workingContractList.get(key)) {
				if(key.equals("contractDate")) {
					String newContractNo=getNewContractNo(value);	
					str.append(newContractNo+",");
				}
				else str.append(value+",");
			}

			str.substring(0, str.length()-1);
			if(key.equals("contractDate")) 
				setValue.put("newContractNo", str.toString()); 
					
			else 
			setValue.put(key, str.toString());
		}
		contractMapper.insertContractDetail(setValue);
		
		resultMap.put("gridRowJson", setValue.get("RESULT"));
		resultMap.put("errorCode", setValue.get("ERROR_CODE"));
		resultMap.put("errorMsg", setValue.get("ERROR_MSG"));

		return resultMap;
	}

	public String getNewContractNo(String contractDate) {
		
		StringBuffer newContractNo = null;

		int i = contractMapper.selectContractCount(contractDate);
		newContractNo = new StringBuffer();
		newContractNo.append("CO"); //CO
		newContractNo.append(contractDate.replace("-", "")); 
		newContractNo.append(String.format("%02d", i));

		return newContractNo.toString();
	}
	
	@Override
	public ModelMap batchContractDetailListProcess(ArrayList<ContractDetailTO> contractDetailTOList) {

		ModelMap resultMap = new ModelMap();

		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();
		
		for (ContractDetailTO bean : contractDetailTOList) {

			String status = bean.getStatus();

			switch (status) {

			case "INSERT":
				
				//contractMapper.insertContractDetail(bean);
				insertList.add(bean.getContractDetailNo());

				break;

			case "UPDATE":
				
				//contractMapper.updateContractDetail(bean);
				updateList.add(bean.getContractDetailNo());

				break;
					
			case "DELETE":

				contractMapper.deleteContractDetail(bean);
				deleteList.add(bean.getContractDetailNo());

				break;

			}

		}

		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public void changeContractStatusInEstimate(String estimateNo, String contractStatus) {

		HashMap<String, String> map = new HashMap<>();

		map.put("estimateNo", estimateNo);
		map.put("contractStatus", contractStatus);
		
		estimateMapper.changeContractStatusOfEstimate(map);

	}

	public void processPlan(HashMap<String,String> processMap) {
		HashMap<String, String> map = new HashMap<>();


		System.out.println("서비스임플 map : "+processMap);
		Set<String> keys = processMap.keySet();
		keys.forEach((key)->{
			System.out.println(processMap.get(key));
			map.put(key,processMap.get(key));
		});
		System.out.println("프로시저 변수값 map : "+map);
		contractMapper.processPlan(map);


	}
}
