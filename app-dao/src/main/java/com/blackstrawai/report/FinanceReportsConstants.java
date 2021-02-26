package com.blackstrawai.report;

public class FinanceReportsConstants {

  public static final String GET_TRIAL_BALANCE_DATA =
      "select\r\n"
          + "    'Liability' LevelType,\r\n"
          + "	COALESCE (coal02.name ,\r\n"
          + "	'Liability_total_sum') level2Name,\r\n"
          + "	coal03.name level3Name,\r\n"
          + "	coal04.name level4Name,\r\n"
          + "	coal05.name level5Name,\r\n"
          + "	coal05.id ledgerId,\r\n"
          + "	jet.particulars journalBookName,\r\n"
          + "	(\r\n"
          + "	select\r\n"
          + "		(sum(amount_credit) - sum(amount_debit))\r\n"
          + "	from\r\n"
          + "		journal_transaction.journal_entries_transaction jet1\r\n"
          + "	where\r\n"
          + "		jet1.particulars = jet.particulars\r\n"
          + "		and jet1.organization_id = ?\r\n"
          + "		and jet1.status = 'ACT'\r\n"
          + "		and jet1.effective_date < ? ) Opening_Balance,\r\n"
          + "	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n"
          + "from\r\n"
          + "	journal_transaction.journal_entries_transaction jet,\r\n"
          + "	usermgmt.chart_of_accounts_level5_organization coal05,\r\n"
          + "	usermgmt.chart_of_accounts_level4_organization coal04,\r\n"
          + "	usermgmt.chart_of_accounts_level3_organization coal03,\r\n"
          + "	usermgmt.chart_of_accounts_level2_organization coal02\r\n"
          + "where\r\n"
          + "	jet.particulars = coal05.name\r\n"
          + "	and coal05.chart_of_accounts_level4_id = coal04.id\r\n"
          + "	and coal04.chart_of_accounts_level3_id = coal03.id\r\n"
          + "	and coal03.chart_of_accounts_level2_id = coal02.id\r\n"
          + "	and coal02.organization_id = coal03.organization_id\r\n"
          + "	and coal03.organization_id = coal04.organization_id\r\n"
          + "	and coal04.organization_id = coal05.organization_id\r\n"
          + "	and coal05.organization_id = jet.organization_id\r\n"
          + "	and jet.organization_id = ?\r\n"
          + "	and jet.status = 'ACT'\r\n"
          + "	and coal02.name in ('Share Application money Received_AllotmentPending'," +
              "'Share Holders Fund','Bank Loans',\n"
          + "'Unsecured Loans',\n"
          + "'Capital Accounts',\n"
          + "'Reserves & Surplus',\n"
          + "'Sundry Debtors',\n"
          + "'Current Liabilities',\n"
          + "'Provisions',\n"
          + "'Duties & Taxes',\n"
          + "'Loans',\n"
          + "'Non-current liabilities')\r\n"
          + "	and jet.effective_date between ? and ?\r\n"
          + "group by\r\n"
          + "	coal02.name,\r\n"
          + "	coal03.name,\r\n"
          + "	coal04.name,\r\n"
          + "	coal05.name,\r\n"
          + "	coal05.id,\r\n"
          + "	jet.particulars with rollup\r\n"
          + "union\r\n"
          + "select\r\n"
          + "    'Asset' LevelType,\r\n"
          + "	coalesce (coal02.name ,\r\n"
          + "	'Asset_total_sum') level2Name,\r\n"
          + "	coal03.name level3Name,\r\n"
          + "	coal04.name level4Name,\r\n"
          + "	coal05.name level5Name,\r\n"
          + "	coal05.id ledgerId,\r\n"
          + "	jet.particulars journalBookName,\r\n"
          + "	(\r\n"
          + "	select\r\n"
          + "		(sum(amount_credit) - sum(amount_debit))\r\n"
          + "	from\r\n"
          + "		journal_transaction.journal_entries_transaction jet1\r\n"
          + "	where\r\n"
          + "		jet1.particulars = jet.particulars\r\n"
          + "		and jet1.organization_id = ?\r\n"
          + "		and jet1.status = 'ACT'\r\n"
          + "		and jet1.effective_date < ? ) Opening_Balance,\r\n"
          + "	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n"
          + "from\r\n"
          + "	journal_transaction.journal_entries_transaction jet,\r\n"
          + "	usermgmt.chart_of_accounts_level5_organization coal05,\r\n"
          + "	usermgmt.chart_of_accounts_level4_organization coal04,\r\n"
          + "	usermgmt.chart_of_accounts_level3_organization coal03,\r\n"
          + "	usermgmt.chart_of_accounts_level2_organization coal02\r\n"
          + "where\r\n"
          + "	jet.particulars = coal05.name\r\n"
          + "	and coal05.chart_of_accounts_level4_id = coal04.id\r\n"
          + "	and coal04.chart_of_accounts_level3_id = coal03.id\r\n"
          + "	and coal03.chart_of_accounts_level2_id = coal02.id\r\n"
          + "	and coal02.organization_id = coal03.organization_id\r\n"
          + "	and coal03.organization_id = coal04.organization_id\r\n"
          + "	and coal04.organization_id = coal05.organization_id\r\n"
          + "	and coal05.organization_id = jet.organization_id\r\n"
          + "	and jet.organization_id = ?\r\n"
          + "	and jet.status = 'ACT'\r\n"
          + "	and coal02.name in ('Current Assets',\r\n"
          + "	'Non Current Assets','Cash and Bank','Fixed Assets','Stock in Hand')\r\n"
          + "	and jet.effective_date between ? and ?\r\n"
          + "group by\r\n"
          + "	coal02.name,\r\n"
          + "	coal03.name,\r\n"
          + "	coal04.name,\r\n"
          + "	coal05.name,\r\n"
          + "	coal05.id,\r\n"
          + "	jet.particulars with rollup\r\n"
          + "union\r\n"
          + "select\r\n"
          + "    'Income' LevelType,\r\n"
          + "	coalesce (coal02.name ,\r\n"
          + "	'Income_total_sum') level2Name,\r\n"
          + "	coal03.name level3Name,\r\n"
          + "	coal04.name level4Name,\r\n"
          + "	coal05.name level5Name,\r\n"
          + "	coal05.id ledgerId,\r\n"
          + "	jet.particulars journalBookName,\r\n"
          + "	(\r\n"
          + "	select\r\n"
          + "		(sum(amount_credit) - sum(amount_debit))\r\n"
          + "	from\r\n"
          + "		journal_transaction.journal_entries_transaction jet1\r\n"
          + "	where\r\n"
          + "		jet1.particulars = jet.particulars\r\n"
          + "		and jet1.organization_id = ?\r\n"
          + "		and jet1.status = 'ACT'\r\n"
          + "		and jet1.effective_date < ? ) Opening_Balance,\r\n"
          + "	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n"
          + "from\r\n"
          + "	journal_transaction.journal_entries_transaction jet,\r\n"
          + "	usermgmt.chart_of_accounts_level5_organization coal05,\r\n"
          + "	usermgmt.chart_of_accounts_level4_organization coal04,\r\n"
          + "	usermgmt.chart_of_accounts_level3_organization coal03,\r\n"
          + "	usermgmt.chart_of_accounts_level2_organization coal02\r\n"
          + "where\r\n"
          + "	jet.particulars = coal05.name\r\n"
          + "	and coal05.chart_of_accounts_level4_id = coal04.id\r\n"
          + "	and coal04.chart_of_accounts_level3_id = coal03.id\r\n"
          + "	and coal03.chart_of_accounts_level2_id = coal02.id\r\n"
          + "	and coal02.organization_id = coal03.organization_id\r\n"
          + "	and coal03.organization_id = coal04.organization_id\r\n"
          + "	and coal04.organization_id = coal05.organization_id\r\n"
          + "	and coal05.organization_id = jet.organization_id\r\n"
          + "	and jet.organization_id = ?\r\n"
          + "	and jet.status = 'ACT'\r\n"
          + "	and coal02.name in ('Income')\r\n"
          + "	and jet.effective_date between ? and ?\r\n"
          + "group by\r\n"
          + "	coal02.name,\r\n"
          + "	coal03.name,\r\n"
          + "	coal04.name,\r\n"
          + "	coal05.name,\r\n"
          + "	coal05.id,\r\n"
          + "	jet.particulars with rollup\r\n"
          + "union\r\n"
          + "select\r\n"
          + "    'Expense' LevelType,\r\n"
          + "	coalesce (coal02.name ,\r\n"
          + "	'Expenses_total_sum') level2Name,\r\n"
          + "	coal03.name level3Name,\r\n"
          + "	coal04.name level4Name,\r\n"
          + "	coal05.name level5Name,\r\n"
          + "	coal05.id ledgerId,\r\n"
          + "	jet.particulars journalBookName,\r\n"
          + "	(\r\n"
          + "	select\r\n"
          + "		(sum(amount_credit) - sum(amount_debit))\r\n"
          + "	from\r\n"
          + "		journal_transaction.journal_entries_transaction jet1\r\n"
          + "	where\r\n"
          + "		jet1.particulars = jet.particulars\r\n"
          + "		and jet1.organization_id = ?\r\n"
          + "		and jet1.status = 'ACT'\r\n"
          + "		and jet1.effective_date < ? ) Opening_Balance,\r\n"
          + "	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n"
          + "from\r\n"
          + "	journal_transaction.journal_entries_transaction jet,\r\n"
          + "	usermgmt.chart_of_accounts_level5_organization coal05,\r\n"
          + "	usermgmt.chart_of_accounts_level4_organization coal04,\r\n"
          + "	usermgmt.chart_of_accounts_level3_organization coal03,\r\n"
          + "	usermgmt.chart_of_accounts_level2_organization coal02\r\n"
          + "where\r\n"
          + "	jet.particulars = coal05.name\r\n"
          + "	and coal05.chart_of_accounts_level4_id = coal04.id\r\n"
          + "	and coal04.chart_of_accounts_level3_id = coal03.id\r\n"
          + "	and coal03.chart_of_accounts_level2_id = coal02.id\r\n"
          + "	and coal02.organization_id = coal03.organization_id\r\n"
          + "	and coal03.organization_id = coal04.organization_id\r\n"
          + "	and coal04.organization_id = coal05.organization_id\r\n"
          + "	and coal05.organization_id = jet.organization_id\r\n"
          + "	and jet.organization_id = ?\r\n"
          + "	and jet.status = 'ACT'\r\n"
          + "	and coal02.name in ('Tax Expense','Expenses')\r\n"
          + "	and jet.effective_date between ? and ?\r\n"
          + "group by\r\n"
          + "	coal02.name,\r\n"
          + "	coal03.name,\r\n"
          + "	coal04.name,\r\n"
          + "	coal05.name,\r\n"
          + "	coal05.id,\r\n"
          + "	jet.particulars with rollup";

  public static final String GET_BALANCE_SHEET_DATA =
      "select\r\n"
          + "    'Liability' LevelType,\r\n"
          + "	COALESCE (coal02.name ,\r\n"
          + "	'Liability_total_sum') level2Name,\r\n"
          + "	coal03.name level3Name,\r\n"
          + "	coal04.name level4Name,\r\n"
          + "	coal05.name level5Name,\r\n"
          + "	coal05.id ledgerId,\r\n"
          + "	jet.particulars journalBookName,\r\n"
          + "	(\r\n"
          + "	select\r\n"
          + "		(sum(amount_credit) - sum(amount_debit))\r\n"
          + "	from\r\n"
          + "		journal_transaction.journal_entries_transaction jet1		\r\n"
          + "	where\r\n"
          + "		jet1.particulars = jet.particulars\r\n"
          + "		and jet1.status = 'ACT'\r\n"
          + "		and jet1.organization_id = ?\r\n"
          + "		and jet1.effective_date < ? ) Opening_Balance,\r\n"
          + "	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n"
          + "from\r\n"
          + "	journal_transaction.journal_entries_transaction jet,\r\n"
          + "	usermgmt.chart_of_accounts_level5_organization coal05,\r\n"
          + "	usermgmt.chart_of_accounts_level4_organization coal04,\r\n"
          + "	usermgmt.chart_of_accounts_level3_organization coal03,\r\n"
          + "	usermgmt.chart_of_accounts_level2_organization coal02\r\n"
          + "where\r\n"
          + "	jet.particulars = coal05.name\r\n"
          + "	and coal05.chart_of_accounts_level4_id = coal04.id\r\n"
          + "	and coal04.chart_of_accounts_level3_id = coal03.id\r\n"
          + "	and coal03.chart_of_accounts_level2_id = coal02.id\r\n"
          + "	and coal02.organization_id = coal03.organization_id\r\n"
          + "	and coal03.organization_id = coal04.organization_id\r\n"
          + "	and coal04.organization_id = coal05.organization_id\r\n"
          + "	and coal05.organization_id = jet.organization_id\r\n"
          + "	and jet.organization_id = ?\r\n"
          + "	and jet.status = 'ACT'\r\n"
          + "	and coal02.name in ('Share Application money Received_AllotmentPending','Share Holders Fund','Bank Loans',\n"
          + "'Unsecured Loans',\n"
          + "'Capital Accounts',\n"
          + "'Reserves & Surplus',\n"
          + "'Sundry Debtors',\n"
          + "'Current Liabilities',\n"
          + "'Provisions',\n"
          + "'Duties & Taxes',\n"
          + "'Loans',\n"
          + "'Non-current liabilities')\r\n"
          + "	and jet.effective_date between ? and ?\r\n"
          + "group by\r\n"
          + "	coal02.name,\r\n"
          + "	coal03.name,\r\n"
          + "	coal04.name,\r\n"
          + "	coal05.name,\r\n"
          + "	coal05.id,\r\n"
          + "	jet.particulars with rollup\r\n"
          + "union\r\n"
          + "select\r\n"
          + "    'Asset' LevelType,\r\n"
          + "	coalesce (coal02.name ,\r\n"
          + "	'Asset_total_sum') level2Name,\r\n"
          + "	coal03.name level3Name,\r\n"
          + "	coal04.name level4Name,\r\n"
          + "	coal05.name level5Name,\r\n"
          + "	coal05.id ledgerId,\r\n"
          + "	jet.particulars journalBookName,\r\n"
          + "	(\r\n"
          + "	select\r\n"
          + "		(sum(amount_credit) - sum(amount_debit))\r\n"
          + "	from\r\n"
          + "		journal_transaction.journal_entries_transaction jet1		\r\n"
          + "	where\r\n"
          + "		jet1.particulars = jet.particulars\r\n"
          + "		and jet1.status = 'ACT'\r\n"
          + "		and jet1.organization_id = ?\r\n"
          + "		and jet1.effective_date < ? ) Opening_Balance,\r\n"
          + "	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n"
          + "from\r\n"
          + "	journal_transaction.journal_entries_transaction jet,\r\n"
          + "	usermgmt.chart_of_accounts_level5_organization coal05,\r\n"
          + "	usermgmt.chart_of_accounts_level4_organization coal04,\r\n"
          + "	usermgmt.chart_of_accounts_level3_organization coal03,\r\n"
          + "	usermgmt.chart_of_accounts_level2_organization coal02\r\n"
          + "where\r\n"
          + "	jet.particulars = coal05.name\r\n"
          + "	and coal05.chart_of_accounts_level4_id = coal04.id\r\n"
          + "	and coal04.chart_of_accounts_level3_id = coal03.id\r\n"
          + "	and coal03.chart_of_accounts_level2_id = coal02.id\r\n"
          + "	and coal02.organization_id = coal03.organization_id\r\n"
          + "	and coal03.organization_id = coal04.organization_id\r\n"
          + "	and coal04.organization_id = coal05.organization_id\r\n"
          + "	and coal05.organization_id = jet.organization_id\r\n"
          + "	and jet.organization_id = ?\r\n"
          + "	and jet.status = 'ACT'\r\n"
          + "	and coal02.name in ('Current Assets',\r\n"
          + "	'Non Current Assets','Cash and Bank','Fixed Assets','Stock in Hand')\r\n"
          + "	and jet.effective_date between ? and ?\r\n"
          + "group by\r\n"
          + "	coal02.name,\r\n"
          + "	coal03.name,\r\n"
          + "	coal04.name,\r\n"
          + "	coal05.name,\r\n"
          + "	coal05.id,\r\n"
          + "	jet.particulars with rollup";

  public static final String GET_BALANCE_SHEET_DATA_ALL =
      "select\r\n"
          + "    'Liability' LevelType,\r\n"
          + "	COALESCE (coal02.name ,\r\n"
          + "	'Liability_total_sum') level2Name,\r\n"
          + "	coal03.name level3Name,\r\n"
          + "	coal04.name level4Name,\r\n"
          + "	coal05.name level5Name,\r\n"
          + "	coal05.id ledgerId,\r\n"
          + "	jet.particulars journalBookName,\r\n"
          + "    null Opening_Balance,\r\n"
          + "	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n"
          + "from\r\n"
          + "	journal_transaction.journal_entries_transaction jet,\r\n"
          + "	usermgmt.chart_of_accounts_level5_organization coal05,\r\n"
          + "	usermgmt.chart_of_accounts_level4_organization coal04,\r\n"
          + "	usermgmt.chart_of_accounts_level3_organization coal03,\r\n"
          + "	usermgmt.chart_of_accounts_level2_organization coal02\r\n"
          + "where\r\n"
          + "	jet.particulars = coal05.name\r\n"
          + "	and coal05.chart_of_accounts_level4_id = coal04.id\r\n"
          + "	and coal04.chart_of_accounts_level3_id = coal03.id\r\n"
          + "	and coal03.chart_of_accounts_level2_id = coal02.id\r\n"
          + "	and coal02.organization_id = coal03.organization_id\r\n"
          + "	and coal03.organization_id = coal04.organization_id\r\n"
          + "	and coal04.organization_id = coal05.organization_id\r\n"
          + "	and coal05.organization_id = jet.organization_id\r\n"
          + "	and jet.status='ACT'\r\n"
          + "	and jet.organization_id = ?\r\n"
          + "	and coal02.name in ('Share Application money Received_AllotmentPending','Share Holders Fund','Bank Loans',\n"
          + "'Unsecured Loans',\n"
          + "'Capital Accounts',\n"
          + "'Reserves & Surplus',\n"
          + "'Sundry Debtors',\n"
          + "'Current Liabilities',\n"
          + "'Provisions',\n"
          + "'Duties & Taxes',\n"
          + "'Loans',\n"
          + "'Non-current liabilities')\r\n"
          + "group by\r\n"
          + "	coal02.name,\r\n"
          + "	coal03.name,\r\n"
          + "	coal04.name,\r\n"
          + "	coal05.name,\r\n"
          + "	coal05.id,\r\n"
          + "	jet.particulars with rollup\r\n"
          + "union\r\n"
          + "select\r\n"
          + "    'Asset' LevelType,\r\n"
          + "	coalesce (coal02.name ,\r\n"
          + "	'Asset_total_sum') level2Name,\r\n"
          + "	coal03.name level3Name,\r\n"
          + "	coal04.name level4Name,\r\n"
          + "	coal05.name level5Name,\r\n"
          + "	coal05.id ledgerId,\r\n"
          + "	jet.particulars journalBookName,\r\n"
          + "	null Opening_Balance,\r\n"
          + "	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n"
          + "from\r\n"
          + "	journal_transaction.journal_entries_transaction jet,\r\n"
          + "	usermgmt.chart_of_accounts_level5_organization coal05,\r\n"
          + "	usermgmt.chart_of_accounts_level4_organization coal04,\r\n"
          + "	usermgmt.chart_of_accounts_level3_organization coal03,\r\n"
          + "	usermgmt.chart_of_accounts_level2_organization coal02\r\n"
          + "where\r\n"
          + "	jet.particulars = coal05.name\r\n"
          + "	and coal05.chart_of_accounts_level4_id = coal04.id\r\n"
          + "	and coal04.chart_of_accounts_level3_id = coal03.id\r\n"
          + "	and coal03.chart_of_accounts_level2_id = coal02.id\r\n"
          + "	and coal02.organization_id = coal03.organization_id\r\n"
          + "	and coal03.organization_id = coal04.organization_id\r\n"
          + "	and coal04.organization_id = coal05.organization_id\r\n"
          + "	and coal05.organization_id = jet.organization_id\r\n"
          + "	and jet.status='ACT'\r\n"
          + "	and jet.organization_id = ?\r\n"
          + "	and coal02.name in ('Current Assets',\r\n"
          + "	'Non Current Assets','Cash and Bank','Fixed Assets','Stock in Hand')\r\n"
          + "group by\r\n"
          + "	coal02.name,\r\n"
          + "	coal03.name,\r\n"
          + "	coal04.name,\r\n"
          + "	coal05.name,\r\n"
          + "	coal05.id,\r\n"
          + "	jet.particulars with rollup";

  public static final String GET_TRIAL_BALANCE_DATA_ALL =
      "select\r\n"
          + "    'Liability' LevelType,\r\n"
          + "	COALESCE (coal02.name ,\r\n"
          + "	'Liability_total_sum') level2Name,\r\n"
          + "	coal03.name level3Name,\r\n"
          + "	coal04.name level4Name,\r\n"
          + "	coal05.name level5Name,\r\n"
          + "	coal05.id ledgerId,\r\n"
          + "	jet.particulars journalBookName,\r\n"
          + "	null Opening_Balance,\r\n"
          + "	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n"
          + "from\r\n"
          + "	journal_transaction.journal_entries_transaction jet,\r\n"
          + "	usermgmt.chart_of_accounts_level5_organization coal05,\r\n"
          + "	usermgmt.chart_of_accounts_level4_organization coal04,\r\n"
          + "	usermgmt.chart_of_accounts_level3_organization coal03,\r\n"
          + "	usermgmt.chart_of_accounts_level2_organization coal02\r\n"
          + "where\r\n"
          + "	jet.particulars = coal05.name\r\n"
          + "	and coal05.chart_of_accounts_level4_id = coal04.id\r\n"
          + "	and coal04.chart_of_accounts_level3_id = coal03.id\r\n"
          + "	and coal03.chart_of_accounts_level2_id = coal02.id\r\n"
          + "	and coal02.organization_id = coal03.organization_id\r\n"
          + "	and coal03.organization_id = coal04.organization_id\r\n"
          + "	and coal04.organization_id = coal05.organization_id\r\n"
          + "	and coal05.organization_id = jet.organization_id\r\n"
          + "	and jet.organization_id = ?\r\n"
          + "	and jet.status='ACT'\r\n"
          + "	and coal02.name in ('Share Application money Received_AllotmentPending','Share Holders Fund','Bank Loans',\n"
          + "'Unsecured Loans',\n"
          + "'Capital Accounts',\n"
          + "'Reserves & Surplus',\n"
          + "'Sundry Debtors',\n"
          + "'Current Liabilities',\n"
          + "'Provisions',\n"
          + "'Duties & Taxes',\n"
          + "'Loans',\n"
          + "'Non-current liabilities')\r\n"
          + "group by\r\n"
          + "	coal02.name,\r\n"
          + "	coal03.name,\r\n"
          + "	coal04.name,\r\n"
          + "	coal05.name,\r\n"
          + "	coal05.id,\r\n"
          + "	jet.particulars with rollup\r\n"
          + "union\r\n"
          + "select\r\n"
          + "    'Asset' LevelType,\r\n"
          + "	coalesce (coal02.name ,\r\n"
          + "	'Asset_total_sum') level2Name,\r\n"
          + "	coal03.name level3Name,\r\n"
          + "	coal04.name level4Name,\r\n"
          + "	coal05.name level5Name,\r\n"
          + "	coal05.id ledgerId,\r\n"
          + "	jet.particulars journalBookName,\r\n"
          + "	null Opening_Balance,\r\n"
          + "	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n"
          + "from\r\n"
          + "	journal_transaction.journal_entries_transaction jet,\r\n"
          + "	usermgmt.chart_of_accounts_level5_organization coal05,\r\n"
          + "	usermgmt.chart_of_accounts_level4_organization coal04,\r\n"
          + "	usermgmt.chart_of_accounts_level3_organization coal03,\r\n"
          + "	usermgmt.chart_of_accounts_level2_organization coal02\r\n"
          + "where\r\n"
          + "	jet.particulars = coal05.name\r\n"
          + "	and coal05.chart_of_accounts_level4_id = coal04.id\r\n"
          + "	and coal04.chart_of_accounts_level3_id = coal03.id\r\n"
          + "	and coal03.chart_of_accounts_level2_id = coal02.id\r\n"
          + "	and coal02.organization_id = coal03.organization_id\r\n"
          + "	and coal03.organization_id = coal04.organization_id\r\n"
          + "	and coal04.organization_id = coal05.organization_id\r\n"
          + "	and coal05.organization_id = jet.organization_id\r\n"
          + "	and jet.status='ACT'\r\n"
          + "	and jet.organization_id = ?\r\n"
          + "	and coal02.name in ('Current Assets',\r\n"
          + "	'Non Current Assets','Cash and Bank','Fixed Assets','Stock in Hand')\r\n"
          + "group by\r\n"
          + "	coal02.name,\r\n"
          + "	coal03.name,\r\n"
          + "	coal04.name,\r\n"
          + "	coal05.name,\r\n"
          + "	coal05.id,\r\n"
          + "	jet.particulars with rollup\r\n"
          + "union\r\n"
          + "select\r\n"
          + "    'Income' LevelType,\r\n"
          + "	coalesce (coal02.name ,\r\n"
          + "	'Income_total_sum') level2Name,\r\n"
          + "	coal03.name level3Name,\r\n"
          + "	coal04.name level4Name,\r\n"
          + "	coal05.name level5Name,\r\n"
          + "	coal05.id ledgerId,\r\n"
          + "	jet.particulars journalBookName,\r\n"
          + "	null Opening_Balance,\r\n"
          + "	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n"
          + "from\r\n"
          + "	journal_transaction.journal_entries_transaction jet,\r\n"
          + "	usermgmt.chart_of_accounts_level5_organization coal05,\r\n"
          + "	usermgmt.chart_of_accounts_level4_organization coal04,\r\n"
          + "	usermgmt.chart_of_accounts_level3_organization coal03,\r\n"
          + "	usermgmt.chart_of_accounts_level2_organization coal02\r\n"
          + "where\r\n"
          + "	jet.particulars = coal05.name\r\n"
          + "	and coal05.chart_of_accounts_level4_id = coal04.id\r\n"
          + "	and coal04.chart_of_accounts_level3_id = coal03.id\r\n"
          + "	and coal03.chart_of_accounts_level2_id = coal02.id\r\n"
          + "	and coal02.organization_id = coal03.organization_id\r\n"
          + "	and coal03.organization_id = coal04.organization_id\r\n"
          + "	and coal04.organization_id = coal05.organization_id\r\n"
          + "	and coal05.organization_id = jet.organization_id\r\n"
          + "	and jet.status='ACT'\r\n"
          + "	and jet.organization_id = ?\r\n"
          + "	and coal02.name in ('Income')\r\n"
          + "group by\r\n"
          + "	coal02.name,\r\n"
          + "	coal03.name,\r\n"
          + "	coal04.name,\r\n"
          + "	coal05.name,\r\n"
          + "	coal05.id,\r\n"
          + "	jet.particulars with rollup\r\n"
          + "union\r\n"
          + "select\r\n"
          + "    'Expense' LevelType,\r\n"
          + "	coalesce (coal02.name ,\r\n"
          + "	'Expenses_total_sum') level2Name,\r\n"
          + "	coal03.name level3Name,\r\n"
          + "	coal04.name level4Name,\r\n"
          + "	coal05.name level5Name,\r\n"
          + "	coal05.id ledgerId,\r\n"
          + "	jet.particulars journalBookName,\r\n"
          + "	null Opening_Balance,\r\n"
          + "	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n"
          + "from\r\n"
          + "	journal_transaction.journal_entries_transaction jet,\r\n"
          + "	usermgmt.chart_of_accounts_level5_organization coal05,\r\n"
          + "	usermgmt.chart_of_accounts_level4_organization coal04,\r\n"
          + "	usermgmt.chart_of_accounts_level3_organization coal03,\r\n"
          + "	usermgmt.chart_of_accounts_level2_organization coal02\r\n"
          + "where\r\n"
          + "	jet.particulars = coal05.name\r\n"
          + "	and coal05.chart_of_accounts_level4_id = coal04.id\r\n"
          + "	and coal04.chart_of_accounts_level3_id = coal03.id\r\n"
          + "	and coal03.chart_of_accounts_level2_id = coal02.id\r\n"
          + "	and coal02.organization_id = coal03.organization_id\r\n"
          + "	and coal03.organization_id = coal04.organization_id\r\n"
          + "	and coal04.organization_id = coal05.organization_id\r\n"
          + "	and coal05.organization_id = jet.organization_id\r\n"
          + "	and jet.status='ACT'\r\n"
          + "	and jet.organization_id = ?\r\n"
          + "	and coal02.name in ('Tax Expense','Expenses','Purchase Accounts')\r\n"
          + "group by\r\n"
          + "	coal02.name,\r\n"
          + "	coal03.name,\r\n"
          + "	coal04.name,\r\n"
          + "	coal05.name,\r\n"
          + "	coal05.id,\r\n"
          + "	jet.particulars with rollup";

	public static final String GET_PROFIT_AND_LOSS_DATA = "select\r\n" + 
			"    'Income' LevelType,\r\n" + 
			"	coalesce (coal02.name ,\r\n" + 
			"	'Income_total_sum') level2Name,\r\n" + 
			"	coal03.name level3Name,\r\n" + 
			"	coal04.name level4Name,\r\n" + 
			"	coal05.name level5Name,\r\n" + 
			"	coal05.id ledgerId,\r\n" +
			"	jet.particulars journalBookName,\r\n" + 
			"	(\r\n" + 
			"	select\r\n" + 
			"		(sum(amount_credit) - sum(amount_debit))\r\n" + 
			"	from\r\n" + 
			"		journal_transaction.journal_entries_transaction jet1\r\n" + 
			"	where\r\n" + 
			"		jet1.particulars = jet.particulars\r\n" + 
			"		and jet1.status='ACT'\r\n" + 
			"		and jet1.organization_id = ?\r\n" + 
			"		and jet1.effective_date < ? ) Opening_Balance,\r\n" + 
			"	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n" + 
			"from\r\n" + 
			"	journal_transaction.journal_entries_transaction jet,\r\n" + 
			"	usermgmt.chart_of_accounts_level5_organization coal05,\r\n" + 
			"	usermgmt.chart_of_accounts_level4_organization coal04,\r\n" + 
			"	usermgmt.chart_of_accounts_level3_organization coal03,\r\n" + 
			"	usermgmt.chart_of_accounts_level2_organization coal02\r\n" + 
			"where\r\n" + 
			"	jet.particulars = coal05.name\r\n" + 
			"	and coal05.chart_of_accounts_level4_id = coal04.id\r\n" + 
			"	and coal04.chart_of_accounts_level3_id = coal03.id\r\n" + 
			"	and coal03.chart_of_accounts_level2_id = coal02.id\r\n" + 
			"	and coal02.organization_id = coal03.organization_id\r\n" + 
			"	and coal03.organization_id = coal04.organization_id\r\n" + 
			"	and coal04.organization_id = coal05.organization_id\r\n" + 
			"	and coal05.organization_id = jet.organization_id\r\n" + 
			"	and jet.status='ACT'\r\n" + 
			"	and jet.organization_id = ?\r\n" + 
			"	and coal02.name in ('Income','Sales Accounts','Indirect Income')\r\n" +
			"	and jet.effective_date between ? and ?\r\n" + 
			"group by\r\n" + 
			"	coal02.name,\r\n" + 
			"	coal03.name,\r\n" + 
			"	coal04.name,\r\n" + 
			"	coal05.name,\r\n" + 
			"	coal05.id,\r\n" + 
			"	jet.particulars with rollup\r\n" + 
			"union\r\n" + 
			"select\r\n" + 
			"    'Expense' LevelType,\r\n" + 
			"	coalesce (coal02.name ,\r\n" + 
			"	'Expenses_total_sum') level2Name,\r\n" + 
			"	coal03.name level3Name,\r\n" + 
			"	coal04.name level4Name,\r\n" + 
			"	coal05.name level5Name,\r\n" + 
			"	coal05.id ledgerId,\r\n" +
			"	jet.particulars journalBookName,\r\n" + 
			"	(\r\n" + 
			"	select\r\n" + 
			"		(sum(amount_credit) - sum(amount_debit))\r\n" + 
			"	from\r\n" + 
			"		journal_transaction.journal_entries_transaction jet1\r\n" + 
			"	where\r\n" + 
			"		jet1.particulars = jet.particulars\r\n" + 
			"		and jet1.status='ACT'\r\n" + 
			"		and jet1.organization_id = ?\r\n" + 
			"		and jet1.effective_date < ? ) Opening_Balance,\r\n" + 
			"	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n" + 
			"from\r\n" + 
			"	journal_transaction.journal_entries_transaction jet,\r\n" + 
			"	usermgmt.chart_of_accounts_level5_organization coal05,\r\n" + 
			"	usermgmt.chart_of_accounts_level4_organization coal04,\r\n" + 
			"	usermgmt.chart_of_accounts_level3_organization coal03,\r\n" + 
			"	usermgmt.chart_of_accounts_level2_organization coal02\r\n" + 
			"where\r\n" + 
			"	jet.particulars = coal05.name\r\n" + 
			"	and coal05.chart_of_accounts_level4_id = coal04.id\r\n" + 
			"	and coal04.chart_of_accounts_level3_id = coal03.id\r\n" + 
			"	and coal03.chart_of_accounts_level2_id = coal02.id\r\n" + 
			"	and coal02.organization_id = coal03.organization_id\r\n" + 
			"	and coal03.organization_id = coal04.organization_id\r\n" + 
			"	and coal04.organization_id = coal05.organization_id\r\n" + 
			"	and coal05.organization_id = jet.organization_id\r\n" + 
			"	and jet.status='ACT'\r\n" + 
			"	and jet.organization_id = ?\r\n" + 
			"	and coal02.name in ('Tax Expense','Expenses','Purchase Accounts')\r\n" +
			"	and jet.effective_date between ? and ?\r\n" + 
			"group by\r\n" + 
			"	coal02.name,\r\n" + 
			"	coal03.name,\r\n" + 
			"	coal04.name,\r\n" + 
			"	coal05.name,\r\n" + 
			"	coal05.id,\r\n" + 
			"	jet.particulars with rollup"; 
	
	public static final String GET_PROFIT_AND_LOSS_ALL_DATA =  "select\r\n" + 
			"    'Income' LevelType,\r\n" + 
			"	coalesce (coal02.name ,\r\n" + 
			"	'Income_total_sum') level2Name,\r\n" + 
			"	coal03.name level3Name,\r\n" + 
			"	coal04.name level4Name,\r\n" + 
			"	coal05.name level5Name,\r\n" + 
			"	coal05.id ledgerId,\r\n" +
			"	jet.particulars journalBookName,\r\n" + 
			"	null Opening_Balance,\r\n" + 
			"	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n" + 
			"from\r\n" + 
			"	journal_transaction.journal_entries_transaction jet,\r\n" + 
			"	usermgmt.chart_of_accounts_level5_organization coal05,\r\n" + 
			"	usermgmt.chart_of_accounts_level4_organization coal04,\r\n" + 
			"	usermgmt.chart_of_accounts_level3_organization coal03,\r\n" + 
			"	usermgmt.chart_of_accounts_level2_organization coal02\r\n" + 
			"where\r\n" + 
			"	jet.particulars = coal05.name\r\n" + 
			"	and coal05.chart_of_accounts_level4_id = coal04.id\r\n" + 
			"	and coal04.chart_of_accounts_level3_id = coal03.id\r\n" + 
			"	and coal03.chart_of_accounts_level2_id = coal02.id\r\n" + 
			"	and coal02.organization_id = coal03.organization_id\r\n" + 
			"	and coal03.organization_id = coal04.organization_id\r\n" + 
			"	and coal04.organization_id = coal05.organization_id\r\n" + 
			"	and coal05.organization_id = jet.organization_id\r\n" + 
			"	and jet.status='ACT'\r\n" + 
			"	and jet.organization_id = ?\r\n" + 
			"	and coal02.name in ('Income','Sales Accounts','Indirect Income')\r\n" +
			"group by\r\n" + 
			"	coal02.name,\r\n" + 
			"	coal03.name,\r\n" + 
			"	coal04.name,\r\n" + 
			"	coal05.name,\r\n" + 
			"	coal05.id,\r\n" +
			"	jet.particulars with rollup\r\n" + 
			"union\r\n" + 
			"select\r\n" + 
			"    'Expense' LevelType,\r\n" + 
			"	coalesce (coal02.name ,\r\n" + 
			"	'Expenses_total_sum') level2Name,\r\n" + 
			"	coal03.name level3Name,\r\n" + 
			"	coal04.name level4Name,\r\n" + 
			"	coal05.name level5Name,\r\n" + 
			"	coal05.id ledgerId,\r\n" +
			"	jet.particulars journalBookName,\r\n" + 
			"	null Opening_Balance,\r\n" + 
			"	(sum(amount_credit) - sum(amount_debit)) Closing_balance\r\n" + 
			"from\r\n" + 
			"	journal_transaction.journal_entries_transaction jet,\r\n" + 
			"	usermgmt.chart_of_accounts_level5_organization coal05,\r\n" + 
			"	usermgmt.chart_of_accounts_level4_organization coal04,\r\n" + 
			"	usermgmt.chart_of_accounts_level3_organization coal03,\r\n" + 
			"	usermgmt.chart_of_accounts_level2_organization coal02\r\n" + 
			"where\r\n" + 
			"	jet.particulars = coal05.name\r\n" + 
			"	and coal05.chart_of_accounts_level4_id = coal04.id\r\n" + 
			"	and coal04.chart_of_accounts_level3_id = coal03.id\r\n" + 
			"	and coal03.chart_of_accounts_level2_id = coal02.id\r\n" + 
			"	and coal02.organization_id = coal03.organization_id\r\n" + 
			"	and coal03.organization_id = coal04.organization_id\r\n" + 
			"	and coal04.organization_id = coal05.organization_id\r\n" + 
			"	and coal05.organization_id = jet.organization_id\r\n" + 
			"	and jet.status='ACT'\r\n" + 
			"	and jet.organization_id = ?\r\n" + 
			"	and coal02.name in ('Tax Expense','Expenses','Purchase Accounts')\r\n" +
			"group by\r\n" + 
			"	coal02.name,\r\n" + 
			"	coal03.name,\r\n" + 
			"	coal04.name,\r\n" + 
			"	coal05.name,\r\n" + 
			"	coal05.id,\r\n" + 
			"	jet.particulars with rollup";

	public static final String GET_REPORT_LEDGER_DATA = "select\r\n" + 
			"    jet.id,\r\n" + 
			"    jet.particulars ,\r\n" + 
			"    jet.effective_date ,\r\n" + 
			"    jet.vendor_invoice_no ,\r\n" + 
			"    jet.vendor_po_no ,\r\n" + 
			"    jet.voucher_no,\r\n" + 
			"    jet.customer_invoice_no ,\r\n" + 
			"    jet.customer_po_no ,\r\n" + 
			"    jet.credit_note_no ,\r\n" + 
			"    jet.debit_note_no ,\r\n" + 
			"    jet.sub_module as 'Type',\r\n" + 
			"    coal05.account_code 'Account_code',\r\n" + 
			"   (\r\n" + 
			"	select\r\n" + 
			"		(sum(amount_credit) - sum(amount_debit))\r\n" + 
			"	from\r\n" + 
			"		journal_transaction.journal_entries_transaction jet1\r\n" + 
			"	where\r\n" + 
			"		jet1.ledger_id = jet.ledger_id \r\n" + 
			"		and jet.status='ACT'\r\n" + 
			"		and jet1.organization_id = ?\r\n" + 
			"		and jet1.effective_date <  ? ) Opening_Balance,\r\n" + 
			"	(select\r\n" + 
			"		(sum(amount_credit) - sum(amount_debit))\r\n" + 
			"	from\r\n" + 
			"		journal_transaction.journal_entries_transaction jet2\r\n" + 
			"	where\r\n" + 
			"		jet2.ledger_id = jet.ledger_id \r\n" + 
			"		and jet.status='ACT'\r\n" + 
			"		and jet2.organization_id = ?\r\n" + 
			"		and jet2.effective_date BETWEEN ? and ? ) Period_balance,\r\n" + 
			"		jet.amount_credit ,\r\n" + 
			"		jet.amount_debit \r\n" + 
			"from\r\n" + 
			"	journal_transaction.journal_entries_transaction jet,\r\n" + 
			"	usermgmt.chart_of_accounts_level5_organization coal05\r\n" + 
			"where\r\n" + 
			"	jet.ledger_id = coal05.id\r\n" + 
			"	and coal05.organization_id = jet.organization_id\r\n" + 
			"	and jet.ledger_id =?\r\n" + 
			"	and jet.status='ACT'\r\n" + 
			"	and jet.organization_id = ?\r\n" + 
			"	and jet.effective_date between ? and ?\r\n" + 
			"	order by jet.id desc";

	public static final String GET_REPORT_LEDGER_DATA_ALL = "select\r\n" + 
			"    jet.id,\r\n" + 
			"    jet.particulars ,\r\n" + 
			"    jet.effective_date ,\r\n" + 
			"    jet.vendor_invoice_no ,\r\n" + 
			"    jet.vendor_po_no ,\r\n" + 
			"    jet.voucher_no,\r\n" + 
			"    jet.customer_invoice_no ,\r\n" + 
			"    jet.customer_po_no ,\r\n" + 
			"    jet.credit_note_no ,\r\n" + 
			"    jet.debit_note_no ,\r\n" + 
			"    jet.sub_module as 'Type',\r\n" + 
			"    coal05.account_code 'Account_code',\r\n" + 
			"   (\r\n" + 
			"	select\r\n" + 
			"		(sum(amount_credit) - sum(amount_debit))\r\n" + 
			"	from\r\n" + 
			"		journal_transaction.journal_entries_transaction jet1\r\n" + 
			"	where\r\n" + 
			"		jet1.ledger_id = jet.ledger_id \r\n" + 
			"		and jet.status='ACT'\r\n" + 
			"		and jet1.organization_id = ?) Opening_Balance,\r\n" + 
			"	(select\r\n" + 
			"		(sum(amount_credit) - sum(amount_debit))\r\n" + 
			"	from\r\n" + 
			"		journal_transaction.journal_entries_transaction jet2\r\n" + 
			"	where\r\n" + 
			"		jet2.ledger_id = jet.ledger_id \r\n" + 
			"		and jet.status='ACT'\r\n" + 
			"		and jet2.organization_id = ? ) Closing_balance,\r\n" + 
			"		jet.amount_credit ,\r\n" + 
			"		jet.amount_debit \r\n" + 
			"from\r\n" + 
			"	journal_transaction.journal_entries_transaction jet,\r\n" + 
			"	usermgmt.chart_of_accounts_level5_organization coal05\r\n" + 
			"where\r\n" + 
			"	jet.ledger_id = coal05.id\r\n" + 
			"	and coal05.organization_id = jet.organization_id\r\n" + 
			"	and jet.ledger_id =?\r\n" + 
			"	and jet.status='ACT'\r\n" + 
			"	and jet.organization_id = ?\r\n" + 
			"	order by jet.id desc";
}
