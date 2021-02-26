package com.blackstrawai.inventorymgmt;

public class InventoryManagementConstants {



	public static final String GET_INV_MGMT_REPORT_DATA_PRODUCTS_LIST = "select distinct PRODUCT_ID , "
			+ "PRODUCT_CATEGORY , PRODUCT_NAME "
			+ " from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit \r\n" +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Sales','Purchase Reversal','Adjustment','Purchase','Sales Reversal') " +
			"and pit.DATE_OF_ENTRY between ? and ? " +
			"and pit.STATUS =? " ;


	public static final String GET_INV_MGMT_REPORT_DATA_QUANTITY_SOLD = "select any_value(PRODUCT_ID) , sum(QUANTITY) as quantity_sold\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit " +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Sales','Purchase Reversal')\r\n" +
			"and pit.DATE_OF_ENTRY between ? and ? " +
			"and pit.STATUS = ? ";



	public static final String GET_INV_MGMT_REPORT_DATA_QUANTITY_BOUGHT = "select any_value(PRODUCT_ID) , sum(QUANTITY) as quantity_sold\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit " +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal') " +
			"and pit.DATE_OF_ENTRY between ? and ? " +
			"and pit.STATUS = ? ";


	public static final String GET_INV_MGMT_REPORT_DATA_QUANTITY_SOLD_PREV = "select any_value(PRODUCT_ID) , sum(QUANTITY) as quantity_sold\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit " +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Sales','Purchase Reversal')\r\n" +
			"and pit.DATE_OF_ENTRY < ? " +
			"and pit.STATUS = ? ";


	public static final String GET_INV_MGMT_REPORT_DATA_QUANTITY_BOUGHT_PREV = "select any_value(PRODUCT_ID) , sum(QUANTITY) as quantity_sold\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit " +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal')\r\n" +
			"and pit.DATE_OF_ENTRY < ? " +
			"and pit.STATUS = ? ";


	public static final String GET_INV_MGMT_REPORT_DATA_RATE_FIFO = " select   PRODUCT_ID , DATE_OF_ENTRY,\r\n" +
			"FIRST_VALUE (RATE) over (\r\n" +
			"partition by PRODUCT_ID\r\n" +
			"order by DATE_OF_ENTRY) \r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit where ORGANIZATION_ID = ? \r\n" +
			"and TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal')\r\n" +
			"and DATE_OF_ENTRY between ?  and ? " +
			"and STATUS = ?  " ;


	public static final String GET_INV_MGMT_REPORT_DATA_RATE_WA = "select any_Value(PRODUCT_ID) , truncate(sum(QUANTITY *RATE ) /sum(QUANTITY ),2) as stock_rate\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit\r\n" +
			"where ORGANIZATION_ID = ? \r\n" +
			"and TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal')\r\n" +
			"and DATE_OF_ENTRY between ? and ? " +
			"and STATUS = ? " ;


	public static final String GET_INV_MGMT_REPORT_DATA_PRODUCTS_LIST_ALL = "select distinct PRODUCT_ID , "
			+ "PRODUCT_CATEGORY , PRODUCT_NAME "
			+ " from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit \r\n" +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Sales','Purchase Reversal','Adjustment','Purchase','Sales Reversal') " +
			"and pit.STATUS =? " ;

	public static final String GET_INV_MGMT_REPORT_DATA_QUANTITY_SOLD_ALL = "select any_value(PRODUCT_ID) , sum(QUANTITY) as quantity_sold\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit " +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Sales','Purchase Reversal')\r\n" +
			"and pit.STATUS = ? ";


	public static final String GET_INV_MGMT_REPORT_DATA_QUANTITY_BOUGHT_ALL = "select any_value(PRODUCT_ID) , sum(QUANTITY) as quantity_sold\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit " +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal') " +
			"and pit.STATUS = ? ";


	public static final String GET_INV_MGMT_REPORT_DATA_QUANTITY_SOLD_PREV_ALL = "select any_value(PRODUCT_ID) , sum(QUANTITY) as quantity_sold\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit " +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Sales','Purchase Reversal')\r\n" +
			"and pit.STATUS = ? ";


	public static final String GET_INV_MGMT_REPORT_DATA_QUANTITY_BOUGHT_PREV_ALL = "select any_value(PRODUCT_ID) , sum(QUANTITY) as quantity_sold\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit " +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal')\r\n" +
			"and pit.STATUS = ? ";


	public static final String GET_INV_MGMT_REPORT_DATA_RATE_FIFO_ALL = " select   PRODUCT_ID , DATE_OF_ENTRY,\r\n" +
			"FIRST_VALUE (RATE) over (\r\n" +
			"partition by PRODUCT_ID\r\n" +
			"order by DATE_OF_ENTRY) \r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit where ORGANIZATION_ID = ? \r\n" +
			"and TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal')\r\n" +
			"and STATUS = ?  " ;

	public static final String GET_INV_MGMT_REPORT_DATA_RATE_WA_ALL = "select any_Value(PRODUCT_ID) , truncate(sum(QUANTITY *RATE ) /sum(QUANTITY ),2) as stock_rate\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit\r\n" +
			"where ORGANIZATION_ID = ? \r\n" +
			"and TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal')\r\n" +
			"and STATUS = ? " ;


	// Defect fixes changes

	public static final String GET_INV_MGMT_REPORT_DATA_PRODUCTS_LIST_V2 =
			"SELECT\n"
					+ "    po.id as id,\n"
					+ "    po.product_id as product_id,\n"
					+ "    any_value(po.name) as product_name,\n"
					+ "    case when any_value(pg.category_name) is null then 'UnCategorized' else any_value(pg.category_name) end as product_category_name,\n"
					+ "    any_value(po.opening_stock_quantity) as opening_stock,\n"
					+ "    any_value(po.opening_stock_value) as opening_stock_value\n"
					+ "FROM\n"
					+ "    usermgmt.product_organization                  po\n"
					+ "    LEFT JOIN inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION   pit ON po.id = pit.product_id\n"
					+ "    LEFT JOIN inventory_mgmt.PRODUCT_CATEGORY                pg ON pg.id = pit.product_category\n"
					+ "WHERE\n"
					+ "    pit.product_id IN (\n"
					+ "        SELECT DISTINCT\n"
					+ "            ipit.product_id\n"
					+ "        FROM\n"
					+ "            inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION ipit\n"
					+ "        WHERE\n"
					+ "            ipit.organization_id = ?\n"
					+ "            AND ipit.transaction_type IN (\n"
					+ "                'Sales',\n"
					+ "                'Purchase Reversal',\n"
					+ "                'Adjustment',\n"
					+ "                'Purchase',\n"
					+ "                'Sales Reversal'\n"
					+ "            )\n"
					+ "            AND ipit.status = ?\n"
					+ "            AND ipit.date_of_entry BETWEEN ? AND ?\n"
					+ "    )\n"
					+ "GROUP BY\n"
					+ " po.id,\n"
					+ " po.product_id ,\n"
					+ " po.name";


	public static final String GET_INV_MGMT_REPORT_DATA_QUANTITY_SOLD_V2 = "select any_value(PRODUCT_ID) as product_id, sum(QUANTITY) as quantity_sold\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit " +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Sales','Purchase Reversal')\r\n" +
			"and pit.PRODUCT_ID in (replace_query_param) " +
			"and pit.STATUS = ? " +
			"and pit.DATE_OF_ENTRY between ? and ? " +
			"GROUP by PRODUCT_ID";

	public static final String GET_INV_MGMT_REPORT_DATA_QUANTITY_BOUGHT_V2 = "select any_value(PRODUCT_ID) as product_id, sum(QUANTITY) as quantity_bought\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit " +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal') " +
			"and pit.PRODUCT_ID in (replace_query_param) " +
			//"and pit.rate <> '' " +
			"and pit.STATUS = ? " +
			"and pit.DATE_OF_ENTRY between ? and ? " +
			"GROUP by PRODUCT_ID ";

  /*public static final String GET_INV_MGMT_REPORT_DATA_RATE_FIFO_V2 = "select   PRODUCT_ID as product_id , DATE_OF_ENTRY,\r\n" +
  "FIRST_VALUE (RATE) over (\r\n" +
  "partition by PRODUCT_ID\r\n" +
  "order by DATE_OF_ENTRY) as stock_rate \r\n" +
  "from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit where ORGANIZATION_ID = ? \r\n" +
  "and TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal')\r\n" +
  "and STATUS = ?  " +
  "and DATE_OF_ENTRY between ?  and ? " +
  "and pit.PRODUCT_ID in (replace_query_param) " +
  //"and  pit.rate <> '' " +
  "order by PRODUCT_ID , RATE ,DATE_OF_ENTRY " ;*/

	public static final String GET_INV_MGMT_REPORT_DATA_RATE_FIFO_V2 = "select   DISTINCT " +
			"LAST_VALUE(pit.RATE) OVER(PARTITION BY pit.PRODUCT_ID ORDER BY pit.DATE_OF_ENTRY ) as stock_rate ,pit.PRODUCT_ID as PRODUCT_ID " +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal') " +
			"and pit.STATUS = ?  " +
			"and pit.DATE_OF_ENTRY between ?  and ? " +
			"and pit.PRODUCT_ID in (replace_query_param) " ;
			//"order by PRODUCT_ID , RATE ,DATE_OF_ENTRY ";

	public static final String GET_INV_MGMT_REPORT_DATA_RATE_WA_V2 = "select any_Value(PRODUCT_ID) as product_id , SUM(TOTAL_COST) as stock_rate , sum(QUANTITY) as quantity\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit\r\n " +
			"where ORGANIZATION_ID = ? \r\n " +
			"and TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal')\r\n " +
			"and STATUS = ? " +
			"and DATE_OF_ENTRY between ? and ? " +
			"and pit.PRODUCT_ID in (replace_query_param) " +
			//"and  pit.rate <> '' " ;
			"group by PRODUCT_ID" ;


	public static final String GET_INV_MGMT_REPORT_DATA_PRODUCTS_LIST_GETALL_V2 =
			"SELECT\n"
					+ "    po.id as id,\n"
					+ "    po.product_id as product_id,\n"
					+ "    any_value(po.name) as product_name,\n"
					+ "    case when any_value(pg.category_name) is null then 'UnCategorized' else any_value(pg.category_name) end as product_category_name,\n"
					+" case when any_value(po.opening_stock_quantity) is null then 0 else any_value(po.opening_stock_quantity) end as opening_stock, "
					+" case when any_value(po.opening_stock_value) is null then 0 else any_value(po.opening_stock_value) end as opening_stock_value "
					+ "FROM\n"
					+ "    usermgmt.product_organization                  po\n"
					+ "    LEFT JOIN inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION   pit ON po.id = pit.product_id\n"
					+ "    LEFT JOIN inventory_mgmt.PRODUCT_CATEGORY                pg ON pg.id = pit.product_category\n"
					+ "WHERE\n"
					+ "    pit.product_id IN (\n"
					+ "        SELECT DISTINCT\n"
					+ "            ipit.product_id\n"
					+ "        FROM\n"
					+ "            inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION ipit\n"
					+ "        WHERE\n"
					+ "            ipit.organization_id = ?\n"
					+ "            AND ipit.transaction_type IN (\n"
					+ "                'Sales',\n"
					+ "                'Purchase Reversal',\n"
					+ "                'Adjustment',\n"
					+ "                'Purchase',\n"
					+ "                'Sales Reversal'\n"
					+ "            )\n"
					//+ "            AND ipit.date_of_entry BETWEEN ? AND ?\n"
					+ "            AND ipit.status = ?\n"
					+ "    )\n"
					+ "GROUP BY\n"
					+ " po.id,\n"
					+ " po.product_id ,\n"
					+ " po.name";


	public static final String GET_INV_MGMT_REPORT_DATA_QUANTITY_SOLD_GETALL_V2 = "select any_value(PRODUCT_ID) as product_id, sum(QUANTITY) as quantity_sold\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit " +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Sales','Purchase Reversal')\r\n" +
			"and pit.PRODUCT_ID in (replace_query_param) " +
			"and pit.STATUS = ? " +
			"GROUP by PRODUCT_ID";

	public static final String GET_INV_MGMT_REPORT_DATA_QUANTITY_BOUGHT_GETALL_V2 = "select any_value(PRODUCT_ID) as product_id, sum(QUANTITY) as quantity_bought\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit " +
			"where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal') " +
			"and pit.PRODUCT_ID in (replace_query_param) " +
			//"and pit.rate <> '' " +
			"and pit.STATUS = ? " +
			"GROUP by PRODUCT_ID ";

	/*public static final String GET_INV_MGMT_REPORT_DATA_RATE_FIFO_GETALL_V2 = "select   PRODUCT_ID as product_id , DATE_OF_ENTRY,\r\n" +
			"FIRST_VALUE (RATE) over (\r\n" +
			"partition by PRODUCT_ID\r\n" +
			"order by DATE_OF_ENTRY) as stock_rate \r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit where ORGANIZATION_ID = ? \r\n" +
				"and TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal')\r\n" +
			"and STATUS = ?  " +
			"and pit.PRODUCT_ID in (replace_query_param) " +
			//"and pit.rate <> '' " +
			"order by PRODUCT_ID , RATE ,DATE_OF_ENTRY " ;*/

	public static final String GET_INV_MGMT_REPORT_DATA_RATE_FIFO_GETALL_V2 = "select   DISTINCT " +
			"LAST_VALUE(pit.RATE) OVER(PARTITION BY pit.PRODUCT_ID ORDER BY pit.DATE_OF_ENTRY ) as stock_rate ,pit.PRODUCT_ID as PRODUCT_ID " +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit where pit.ORGANIZATION_ID = ? " +
			"and pit.TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal') " +
			"and pit.STATUS = ?  " +
			"and pit.PRODUCT_ID in (replace_query_param) " ;
	//"order by PRODUCT_ID , RATE ,DATE_OF_ENTRY ";

	public static final String GET_INV_MGMT_REPORT_DATA_RATE_WA_GETALL_V2 = "select any_Value(PRODUCT_ID) as product_id , SUM(TOTAL_COST) as stock_rate , sum(QUANTITY) as quantity\r\n" +
			"from  inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION pit\r\n" +
			"where ORGANIZATION_ID = ? \r\n" +
			"and TRANSACTION_TYPE in ('Adjustment','Purchase','Sales Reversal')\r\n" +
			"and STATUS = ? " +
			"and pit.PRODUCT_ID in (replace_query_param) " +
			//"and  pit.rate <> '' " ;
			"group by PRODUCT_ID" ;
}
