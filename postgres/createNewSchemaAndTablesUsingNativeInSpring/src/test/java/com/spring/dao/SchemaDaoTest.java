package com.spring.dao;

import com.spring.MainClass;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MainClass.class)
public class SchemaDaoTest {

    @Autowired private SchemaDao schemaDao;

    private static final String customerId = "cust_123456789";

    @Test public void createSchemaTest() {
        schemaDao.createSchema(customerId);
    }

    @Test @DisplayName("Create customer namespace - all the customer tables")
    public void createCustomerNameSpaceTest() {
        schemaDao.createCustomerNameSpace(customerId);
    }

    @Test public void createAppRootsPolicyPrefHistoryTableTest() {
        schemaDao.createAppRootsPolicyPrefHistoryTable(customerId);
        System.out.println("Success");
    }

    @Test public void createAppRootsPolicyPreferenceTableTest() {
        schemaDao.createAppRootsPolicyPreferenceTable(customerId);
        System.out.println("Success");
    }

    @Test public void createApplicationRootsTableTest() {
        schemaDao.createApplicationRootsTable(customerId);
        System.out.println("Success");
    }

    @Test public void createAutomarkFPRulesTableTest() {
        schemaDao.createAutomarkFPRulesTable(customerId);
        System.out.println("Success");
    }

    @Test public void createCertsTableTest() {
        schemaDao.createCertsTable(customerId);
        System.out.println("Success");
    }

    @Test public void createClientHardeningProjectAccessControlListTableTest() {
        schemaDao.createClientHardeningProjectAccessControlListTable(customerId);
        System.out.println("Success");
    }

    @Test public void createClientHardeningProjectTableTest() {
        schemaDao.createClientHardeningProjectTable(customerId);
        System.out.println("Success");
    }

    @Test public void createClientHardeningProjectAuthenticationParametersTableTest() {
        schemaDao.createClientHardeningProjectAuthenticationParametersTable(customerId);
        System.out.println("Success");
    }

    @Test public void createCspPolicyTableTest() {
        schemaDao.createCspPolicyTable(customerId);
        System.out.println("Success");
    }

    @Test public void createDeploymentProfilesTableTest() {
        schemaDao.createDeploymentProfilesTable(customerId);
        System.out.println("Success");
    }

    @Test public void createGlobalWhiteListTableTest() {
        schemaDao.createGlobalWhiteListTable(customerId);
        System.out.println("Success");
    }

    @Test public void createHmacKeysTableTest() {
        schemaDao.createHmacKeysTable(customerId);
        System.out.println("Success");
    }

    //TODO: Do it
    @Test public void createHstsGlobalSettingsTableTest() {
        schemaDao.createHstsGlobalSettingsTable(customerId);
        System.out.println("Success");
    }

    @Test public void createHstsGlobalSettingsHistoryTableTest() {
        schemaDao.createHstsGlobalSettingsHistoryTable(customerId);
        System.out.println("Success");
    }

    @Test public void createIncrementalScanInstanceTableTest() {
        schemaDao.createIncrementalScanInstanceTable(customerId);
        System.out.println("Success");
    }

    @Test public void createPiiEncryptionKeysTableTest() {
        schemaDao.createPiiEncryptionKeysTable(customerId);
        System.out.println("Success");
    }

    @Test public void createProjectSecurityGroupTableTest() {
        schemaDao.createProjectSecurityGroupTable(customerId);
        System.out.println("Success");
    }

    @Test public void createReportStatusTableTest() {
        schemaDao.createReportStatusTable(customerId);
        System.out.println("Success");
    }

    @Test public void createScanInstanceTableTest() {
        schemaDao.createScanInstanceTable(customerId);
        System.out.println("Success");
    }

    @Test public void createScanPolicyPreferencesTableTest() {
        schemaDao.createScanPolicyPreferencesTable(customerId);
        System.out.println("Success");
    }

    @Test public void createScanDataTableTest() {
        schemaDao.createScanDataTable(customerId);
        System.out.println("Success");
    }

    @Test public void createScanScopeTableTest() {
        schemaDao.createScanScopeTable(customerId);
        System.out.println("Success");
    }

    @Test public void createScriptLogTableTest() {
        schemaDao.createScriptLogTable(customerId);
        System.out.println("Success");
    }

    @Test public void createSecurityGroupsTableTest() {
        schemaDao.createSecurityGroupsTable(customerId);
        System.out.println("Success");
    }

    @Test public void createSecuritySettingsTableTest() {
        schemaDao.createSecuritySettingsTable(customerId);
        System.out.println("Success");
    }

    @Test public void createSyntheticTransactionsTableTest() {
        schemaDao.createSyntheticTransactionsTable(customerId);
        System.out.println("Success");
    }

    @Test public void createUserActionLogsTableTest() {
        schemaDao.createUserActionLogsTable(customerId);
        System.out.println("Success");
    }
}
