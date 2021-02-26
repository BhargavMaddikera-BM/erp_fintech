package com.blackstrawai;

import java.sql.Date;

public class Test {

public static void main(String[] args)throws Exception {
		/*ObjectMapper mapper = new ObjectMapper();
		ArInvoiceVo ar = new ArInvoiceVo();
		ArInvoiceGeneralInformationVo  vo = new ArInvoiceGeneralInformationVo();
		CustomerBillingAddressVo ba = new CustomerBillingAddressVo();
		CustomerDeliveryAddressVo da = new CustomerDeliveryAddressVo();
		vo.setBillingAddress(ba);
		vo.setDeliveryAddress(da);
		ArInvoiceProductVo prod = new ArInvoiceProductVo();
		ArInvoiceTaxDetailsVo tax = new ArInvoiceTaxDetailsVo();
		List<ArInvoiceTaxDistributionVo> lis = new ArrayList<ArInvoiceTaxDistributionVo>();
		List<ArInvoiceProductVo> list = new ArrayList<ArInvoiceProductVo>();
		ArInvoiceTaxDistributionVo dis = new ArInvoiceTaxDistributionVo() ;
		lis.add(dis);
		tax.setTaxDistribution(lis);
		prod.setTaxDetails(tax);
		list.add(prod);
		ar.setProducts(list);
		ar.setGeneralInformation(vo);;
		String jsonString = mapper.writeValueAsString(new QuickInvoiceRequest() );	
		System.out.println(jsonString);
		
		Double val = 0.00;
		if(val.equals(0.00)) {
			System.out.println("Tre");
		}*/
	String str="2016-12-31";
	int val=str.indexOf("-");
	String year=str.substring(0,val);
	String month=str.substring(str.indexOf("-")+1,str.lastIndexOf("-"));
	String day=str.substring(str.lastIndexOf("-")+1,str.length());
	Date txnDate=new Date(Integer.parseInt(year)-1900,Integer.parseInt(month)-1,Integer.parseInt(day));
	System.out.println(txnDate);
	
	
		
	
		// TODO Auto-generated method stub
		
	//	EmailUtil.getInstance().sendEmail("bhargav.maddikera@blackstraw.ai", "bhargav.maddikera@blackstraw.ai", "hi", "hi");

	}
	//
	//https://stackoverflow.com/questions/35347269/javax-mail-authenticationfailedexception-535-5-7-8-username-and-password-not-ac
	
	/*	 public static void main(String[] args) throws Exception{
		/* DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		 Date date = dateFormat.parse("2020-05-10T18:30:00.000Z");
		 DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		 String dateStr = formatter.format(date);
		 System.out.println(dateStr);
		 
		   TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse("2020-05-09T18:30:00.000Z");
	        Instant in = Instant.from(ta);
	      //  Date datoe = Date.from(i);
	      //  System.out.println(new Timestamp(date.getTime()));
	        
	       // Date da = new Dat
	      //  Timestamp  ts = new Timestamp(date.getTime());
	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	       // String convertedDate =dateFormat.format(ts);
			//logger.info("Date picker date converted:"+convertedDate);
	        
	        String value = "acb";
	        List<String> values = new ArrayList<String>();
	        for(int i = 0;i<value.length();i++) {
	        	for(int j=i+1;j<=value.length();j++) {
	        		System.out.println("i is::"+i+"j is::"+j+"Values is ::"+value.substring(i, j));
	        		values.add(value.substring(i, j));
	        	}
	        }
		/*
		 * String val2 = new StringBuilder(value).reverse().toString(); for(int i =
		 * 0;i<val2.length();i++) { for(int j=i+1;j<=val2.length();j++) {
		 * System.out.println("i is::"+i+"j is::"+j+"Values is ::"+val2.substring(i,
		 * j)); values.add(val2.substring(i, j)); } }
		 */
	/*     Set<String> uniqSet = new HashSet<String>();
	        uniqSet.addAll(values);
	        
	        Collections.sort(values);
	        Integer  year = 2025 % 100;
	        

	        System.out.println("Sorted list ::"+ year );
	    }
*/
}
