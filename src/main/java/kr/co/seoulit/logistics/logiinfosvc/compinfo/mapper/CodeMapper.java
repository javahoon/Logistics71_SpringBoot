package kr.co.seoulit.logistics.logiinfosvc.compinfo.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CodeMapper {

	public ArrayList<CodeTO> selectCodeList();

	public void insertCode(CodeTO codeTO);

	public void updateCode(CodeTO codeTO);

	public void deleteCode(CodeTO codeTO);

	public ArrayList<CustomerTO> allCustomerList();
	//codeDetail

	ArrayList<CodeDetailTO> selectDetailCodeList(String divisionCode);

	void insertDetailCode(CodeDetailTO TO);

	void updateDetailCode(CodeDetailTO TO);

	public void deleteDetailCode(CodeDetailTO TO);
	
	public void changeCodeUseCheck(HashMap<String, String> map);
	
	public ArrayList<LatLngTO> selectLatLngList(String wareHouseCodeNo);
	
	public ArrayList<ImageTO> selectDetailItemList(String itemGroupCodeNo);
	public void registerCustomer(CustomerTO to);
	
}
