package com.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Repository @Transactional public class SchemaDao {

    @Autowired @Qualifier("configEntityManagerFactory") private EntityManager entityManager;

    @Value("${db.user}") private String dbUser;

    public void createSchema(String customerId) {
        Query query = entityManager
            .createNativeQuery("CREATE SCHEMA " + customerId + " AUTHORIZATION " + dbUser);
        query.executeUpdate();
    }

    public void createCustomerNameSpace(String customerId) {
        createSchema(customerId);
        createApplicationRootsTable(customerId);
        createAutomarkFPRulesTable(customerId);
        createClientHardeningProjectAccessControlListTable(customerId);
        createClientHardeningProjectTable(customerId);
        createCspPolicyTable(customerId);
        createDeploymentProfilesTable(customerId);
        createGlobalWhiteListTable(customerId);
        createHstsGlobalSettingsTable(customerId);
        createHstsGlobalSettingsHistoryTable(customerId);
        createPiiEncryptionKeysTable(customerId);
        createReportStatusTable(customerId);
        createScanDataTable(customerId);
        createScanScopeTable(customerId);
        createScriptLogTable(customerId);
        createSecurityGroupsTable(customerId);
        createSecuritySettingsTable(customerId);
        createSyntheticTransactionsTable(customerId);
        createUserActionLogsTable(customerId);
        createAppRootsPolicyPrefHistoryTable(customerId);
        createAppRootsPolicyPreferenceTable(customerId);
        createCertsTable(customerId);
        createClientHardeningProjectAuthenticationParametersTable(customerId);
        createHmacKeysTable(customerId);
        createIncrementalScanInstanceTable(customerId);
        createProjectSecurityGroupTable(customerId);
        createScanInstanceTable(customerId);
        createScanPolicyPreferencesTable(customerId);
    }

    public void createAppRootsPolicyPrefHistoryTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
            "CREATE TABLE " + customerId + ".app_roots_policy_pref_history (\n"
                + "\tid serial NOT NULL,\n" + "\tapp_root_id int4 NULL,\n"
                + "\tscan_preference_id int4 NOT NULL,\n" + "\tscandata_id int4 NOT NULL,\n"
                + "\tcreated timestamp NOT NULL DEFAULT now(),\n"
                + "\tenforcement_type text NOT NULL DEFAULT 'report-only',\n"
                + "\tblock_mixed_content bool NULL DEFAULT true,\n"
                + "\tframe_ancestors text NULL,\n"
                + "\treport_uri text NULL DEFAULT 'tala-cloud',\n"
                + "\tcustom_policy_directives json NULL,\n"
                + "\tsri_enabled bool NOT NULL DEFAULT false,\n" + "\tsri_scripts json NULL,\n"
                + "\tsri_updated timestamptz NULL,\n"
                + "\trewrite_problematic_constructs bool NOT NULL DEFAULT false,\n"
                + "\tsri_fallback_enabled bool NOT NULL DEFAULT false,\n"
                + "\tsri_reporting_enabled bool NOT NULL DEFAULT false,\n"
                + "\tenable_nonce bool NOT NULL DEFAULT false,\n"
                + "\treferrer_header_values json NULL,\n" + "\tcustom_domains json NULL,\n"
                + "\timg_src_wildcard json NULL DEFAULT '[\"https:\",\"http:\"]',\n"
                + "\trule_id int4 NULL,\n" + "\tfeature_policy json NULL,\n"
                + "\tiframe_sandboxing json NULL,\n"
                + "\tCONSTRAINT app_roots_policy_pref_history_pkey PRIMARY KEY (id),\n"
                + "\tCONSTRAINT app_roots_policy_pref_history_scandata_id_foreign FOREIGN KEY (scandata_id) REFERENCES "+customerId+".scandata(scandata_id)\n"
                + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Index
        query = entityManager.createNativeQuery(
            "CREATE INDEX app_roots_policy_pref_history_scandata_id_index ON " + customerId
                + ".app_roots_policy_pref_history USING btree (scandata_id);");
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".app_roots_policy_pref_history OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".app_roots_policy_pref_history TO " + dbUser);
        query.executeUpdate();
    }

    public void createAppRootsPolicyPreferenceTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            "CREATE TABLE " + customerId + ".app_roots_policy_preference (\n"
                + "\tid serial NOT NULL,\n" + "\tapp_root_id int4 NULL,\n"
                + "\tscan_preference_id int4 NOT NULL,\n" + "\tscandata_id int4 NOT NULL,\n"
                + "\tcreated timestamp NOT NULL DEFAULT now(),\n"
                + "\tenforcement_type text NOT NULL DEFAULT 'report-only',\n"
                + "\tblock_mixed_content bool NULL DEFAULT true,\n"
                + "\tframe_ancestors text NULL,\n"
                + "\treport_uri text NULL DEFAULT 'tala-cloud',\n"
                + "\tcustom_policy_directives json NULL,\n"
                + "\tsri_enabled bool NOT NULL DEFAULT false,\n" + "\tsri_scripts json NULL,\n"
                + "\tsri_updated timestamptz NULL,\n"
                + "\trewrite_problematic_constructs bool NOT NULL DEFAULT false,\n"
                + "\tsri_fallback_enabled bool NOT NULL DEFAULT false,\n"
                + "\tsri_reporting_enabled bool NOT NULL DEFAULT false,\n"
                + "\tenable_nonce bool NOT NULL DEFAULT false,\n"
                + "\treferrer_header_values json NULL,\n" + "\tcustom_domains json NULL,\n"
                + "\timg_src_wildcard json NULL DEFAULT '[\"https:\",\"http:\"]',\n"
                + "\trule_id int4 NULL,\n" + "\tfeature_policy json NULL,\n"
                + "\tiframe_sandboxing json NULL,\n"
                + "\tCONSTRAINT app_roots_policy_preference_pkey PRIMARY KEY (id),\n"
                + "\tCONSTRAINT app_roots_policy_preference_scandata_id_foreign FOREIGN KEY (scandata_id) REFERENCES "
                + customerId + ".scandata(scandata_id)\n" + ")");
        query.executeUpdate();

        //Index
        query = entityManager.createNativeQuery(
            "CREATE INDEX app_roots_policy_preference_scandata_id_index ON " + customerId
                + ".app_roots_policy_preference USING btree (scandata_id)");
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            //@formatter:off
            "create trigger inserthistoryonupdate before\n"
                + "delete\n"
                + "    or\n"
                + "update\n"
                + "    on\n"
                + "    "+customerId+".app_roots_policy_preference for each row execute procedure inserthistory()\n"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".app_roots_policy_preference OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".app_roots_policy_preference TO " + dbUser);
        query.executeUpdate();
    }

    public void createApplicationRootsTable(String customerId) {
        //create table
        Query query = entityManager.createNativeQuery(
            //@formatter:off
            "CREATE TABLE " + customerId + ".application_roots (\n"
                + "\tid serial NOT NULL,\n"
                + "\tapplication_root_url text NULL,\n"
                + "\tCONSTRAINT application_roots_application_root_url_unique UNIQUE (application_root_url),\n"
                + "\tCONSTRAINT application_roots_pkey PRIMARY KEY (id)\n"
                + ")"
        //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".application_roots OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".application_roots TO " + dbUser);
        query.executeUpdate();
    }

    public void createAutomarkFPRulesTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE " + customerId + ".automark_fp_rules (\n"
              + "\tid serial NOT NULL,\n" + "\trules json NOT NULL,\n"
              + "\trule_hash text NOT NULL,\n" + "\tadded timestamptz NOT NULL DEFAULT now(),\n"
              + "\tCONSTRAINT automark_fp_rules_pkey PRIMARY KEY (id),\n"
              + "\tCONSTRAINT automark_fp_rules_rule_hash_unique UNIQUE (rule_hash)\n" + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".automark_fp_rules OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".automark_fp_rules TO " + dbUser);
        query.executeUpdate();
    }

    public void createCertsTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
            "CREATE TABLE " + customerId + ".certs (\n" + "\tid serial NOT NULL,\n"
                + "\tsubject_dn text NULL,\n" + "\tfilename text NULL,\n" + "\tfile bytea NULL,\n"
                + "\tdate_added timestamptz NULL,\n" + "\tdate_modified timestamptz NULL,\n"
                + "\tsecurity_group_id int4 NOT NULL,\n"
                + "\tCONSTRAINT certs_pkey PRIMARY KEY (id),\n"
                + "\tCONSTRAINT certs_security_group_id_foreign FOREIGN KEY (security_group_id) REFERENCES "+customerId+".security_groups(id)\n"
                + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager
            .createNativeQuery("ALTER TABLE " + customerId + ".certs OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager
            .createNativeQuery("GRANT ALL ON TABLE " + customerId + ".certs TO " + dbUser);
        query.executeUpdate();
    }

    public void createClientHardeningProjectAccessControlListTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            "CREATE TABLE " + customerId + ".client_hardening_project_access_control_list (\n"
                + "\tid serial NOT NULL,\n" + "\tcreated timestamp NOT NULL DEFAULT now(),\n"
                + "\tclient_hardening_project_id int4 NOT NULL,\n" + "\tuser_id int4 NOT NULL,\n"
                + "\tpermissions int4 NOT NULL DEFAULT 0,\n"
                + "\tCONSTRAINT client_hardening_project_access_control_list_pkey PRIMARY KEY (id)\n"
                + ")");
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".client_hardening_project_access_control_list OWNER TO "
                + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".client_hardening_project_access_control_list TO "
                + dbUser);
        query.executeUpdate();
    }

    public void createClientHardeningProjectTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            "CREATE TABLE " + customerId + ".client_hardeninig_project (\n"
                + "\tclient_hardeninig_project_id serial NOT NULL,\n"
                + "\tcreated timestamp NOT NULL DEFAULT now(),\n" + "\t\"name\" text NULL,\n"
                + "\turl text NULL,\n" + "\tstate text NOT NULL DEFAULT 'AVAILABLE',\n"
                + "\tdeleted timestamp NULL,\n" + "\t\"type\" text NULL,\n"
                + "\t\"owner\" int4 NOT NULL,\n" + "\tis_intranet bool NOT NULL DEFAULT false,\n"
                + "\tCONSTRAINT client_hardeninig_project_pkey PRIMARY KEY (client_hardeninig_project_id),\n"
                + "\tCONSTRAINT client_hardeninig_project_state_check CHECK ((state = ANY (ARRAY['AVAILABLE', 'DELETE_PENDING', 'POSTGRES_DELETED', 'DELETED'])))\n"
                + ")");
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".client_hardeninig_project OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".client_hardeninig_project TO " + dbUser);
        query.executeUpdate();
    }

    public void createClientHardeningProjectAuthenticationParametersTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".client_hardeninig_project_authentication_parameters (\n"
              + "\tclient_hardeninig_project_authentication_parameters_id serial NOT NULL,\n"
              + "\tapp_login_url text NULL,\n" + "\tapp_logout_url text NULL,\n"
              + "\tform_element_type text NULL,\n" + "\tform_element text NULL,\n"
              + "\tsubmit_button_type text NULL,\n" + "\tsubmit_button text NULL,\n"
              + "\tusername_type text NULL,\n" + "\tusername text NULL,\n"
              + "\tusername_value text NULL,\n" + "\tpassword_type text NULL,\n"
              + "\t\"password\" text NULL,\n" + "\tpassword_value text NULL,\n"
              + "\tscandata_id int4 NOT NULL,\n"
              + "\tCONSTRAINT client_hardeninig_project_authentication_parameters_pkey PRIMARY KEY (client_hardeninig_project_authentication_parameters_id),\n"
              + "\tCONSTRAINT client_hardeninig_project_authentication_parameters_scandata_id FOREIGN KEY (scandata_id) REFERENCES "+customerId+".scandata(scandata_id)\n"
              + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Index
        query = entityManager.createNativeQuery(
            "CREATE INDEX client_hardeninig_project_authentication_parameters_scandata_id ON "
                + customerId
                + ".client_hardeninig_project_authentication_parameters USING btree (scandata_id);");
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery("ALTER TABLE " + customerId
            + ".client_hardeninig_project_authentication_parameters OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery("GRANT ALL ON TABLE " + customerId
            + ".client_hardeninig_project_authentication_parameters TO " + dbUser);
        query.executeUpdate();
    }

    public void createCspPolicyTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            "CREATE TABLE " + customerId + ".csp_policy (\n"
                + "\tclient_hardeninig_project_id int4 NOT NULL,\n"
                + "\tcsp_policy_id serial NOT NULL,\n"
                + "\tcreated timestamp NOT NULL DEFAULT now(),\n" + "\tpolicy text NULL,\n"
                + "\tCONSTRAINT csp_policy_pkey PRIMARY KEY (csp_policy_id),\n"
                + "\tCONSTRAINT csp_policy_client_hardeninig_project_id_foreign FOREIGN KEY (client_hardeninig_project_id) REFERENCES "
                + customerId + ".client_hardeninig_project(client_hardeninig_project_id)\n" + ")");
        query.executeUpdate();

        //Index
        query = entityManager.createNativeQuery(
            "CREATE INDEX csp_policy_client_hardeninig_project_id_index ON " + customerId
                + ".csp_policy USING btree (client_hardeninig_project_id);");

        query.executeUpdate();

        //Permissions
        query = entityManager
            .createNativeQuery("ALTER TABLE " + customerId + ".csp_policy OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager
            .createNativeQuery("GRANT ALL ON TABLE " + customerId + ".csp_policy TO " + dbUser);
        query.executeUpdate();
    }

    public void createDeploymentProfilesTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".deployment_profiles (\n"
              + "\tid serial NOT NULL,\n" + "\t\"name\" text NOT NULL,\n"
              + "\tprofile json NOT NULL,\n" + "\tproject_id int4 NOT NULL,\n"
              + "\tprofile_type int4 NULL,\n" + "\tadded timestamptz NOT NULL DEFAULT now(),\n"
              + "\tlast_modified timestamptz NOT NULL DEFAULT now(),\n"
              + "\tCONSTRAINT deployment_profiles_pkey PRIMARY KEY (id),\n"
              + "\tCONSTRAINT deployment_profiles_project_id_foreign FOREIGN KEY (project_id) REFERENCES "+customerId+".client_hardeninig_project(client_hardeninig_project_id)\n"
              + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Index
        query = entityManager.createNativeQuery(
            "CREATE INDEX deployment_profiles_project_id_index ON " + customerId
                + ".deployment_profiles USING btree (project_id);");

        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".deployment_profiles OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".deployment_profiles TO " + dbUser);
        query.executeUpdate();
    }

    public void createGlobalWhiteListTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".global_whitelist (\n"
              + "\tid serial NOT NULL,\n" + "\twhitelist json NULL,\n"
              + "\tadded timestamptz NOT NULL DEFAULT now(),\n"
              + "\tlast_modified timestamptz NOT NULL DEFAULT now(),\n"
              + "\tCONSTRAINT global_whitelist_pkey PRIMARY KEY (id)\n" + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".global_whitelist OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".global_whitelist TO " + dbUser);
        query.executeUpdate();
    }

    public void createHmacKeysTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            "CREATE TABLE " + customerId + ".hmac_keys (\n" + "\tid serial NOT NULL,\n"
                + "\tkey_name text NOT NULL,\n" + "\t\"key\" bytea NOT NULL,\n"
                + "\tdate_added timestamptz NOT NULL DEFAULT now(),\n"
                + "\tdate_modified timestamptz NULL,\n" + "\tsecurity_group_id int4 NOT NULL,\n"
                + "\tCONSTRAINT hmac_keys_pkey PRIMARY KEY (id),\n"
                + "\tCONSTRAINT hmac_keys_security_group_id_foreign FOREIGN KEY (security_group_id) REFERENCES "
                + customerId + ".security_groups(id)\n" + ")");
        query.executeUpdate();

        //Permissions
        query = entityManager
            .createNativeQuery("ALTER TABLE " + customerId + ".hmac_keys OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager
            .createNativeQuery("GRANT ALL ON TABLE " + customerId + ".hmac_keys TO " + dbUser);
        query.executeUpdate();
    }

    //TODO: Do it
    public void createHstsGlobalSettingsTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".hsts_global_settings (\n"
              + "\tid serial NOT NULL,\n" + "\t\"domain\" text NULL,\n" + "\thsts json NULL,\n"
              + "\tadded timestamp NOT NULL DEFAULT now(),\n"
              + "\tlast_updated timestamp NOT NULL DEFAULT now(),\n"
              + "\tis_active bool NULL DEFAULT false,\n"
              + "\tCONSTRAINT hsts_global_settings_pkey PRIMARY KEY (id)\n" + ")"
            //@formatter:on
        );
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            //@formatter:off
            "create trigger inserthstshistoryonupdate before\n"
                + "update\n"
                + "    of last_updated on\n" + "    "
                + customerId+ ".hsts_global_settings for each row execute procedure public.insert_hsts_history()\n"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".hsts_global_settings OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".hsts_global_settings TO " + dbUser);
        query.executeUpdate();
    }

    public void createHstsGlobalSettingsHistoryTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            "CREATE TABLE " + customerId + ".hsts_global_settings_history (\n"
                + "\tid serial NOT NULL,\n" + "\t\"domain\" text NULL,\n" + "\thsts json NULL,\n"
                + "\tadded timestamp NOT NULL DEFAULT now(),\n"
                + "\tlast_updated timestamp NOT NULL DEFAULT now(),\n"
                + "\tis_active bool NULL DEFAULT false,\n"
                + "\tCONSTRAINT hsts_global_settings_history_pkey PRIMARY KEY (id)\n" + ")");
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".hsts_global_settings_history OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".hsts_global_settings_history TO " + dbUser);
        query.executeUpdate();
    }

    public void createIncrementalScanInstanceTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            "CREATE TABLE " + customerId + ".incremental_scan_instance (\n"
                + "\tid serial NOT NULL,\n" + "\tunique_id text NULL,\n"
                + "\tcreated timestamp NOT NULL DEFAULT now(),\n" + "\tcompleted timestamp NULL,\n"
                + "\tscan_completed timestamp NULL,\n" + "\terror text NULL,\n"
                + "\tstate int4 NULL,\n" + "\tscandata_id int4 NOT NULL,\n"
                + "\tCONSTRAINT incremental_scan_instance_pkey PRIMARY KEY (id),\n"
                + "\tCONSTRAINT incremental_scan_instance_scandata_id_foreign FOREIGN KEY (scandata_id) REFERENCES "
                + customerId + ".scandata(scandata_id)\n" + ")");
        query.executeUpdate();

        //Index
        query = entityManager.createNativeQuery(
            "CREATE INDEX incremental_scan_instance_scandata_id_index ON " + customerId
                + ".incremental_scan_instance USING btree (scandata_id);");

        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".incremental_scan_instance OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".incremental_scan_instance TO " + dbUser);
        query.executeUpdate();
    }

    public void createPiiEncryptionKeysTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".pii_encryption_keys (\n"
              + "\tid serial NOT NULL,\n" + "\tkey_name text NOT NULL,\n"
              + "\t\"key\" bytea NOT NULL,\n" + "\tiv bytea NOT NULL,\n"
              + "\tdate_added timestamptz NOT NULL DEFAULT now(),\n"
              + "\tdate_modified timestamptz NULL,\n"
              + "\tCONSTRAINT pii_encryption_keys_pkey PRIMARY KEY (id)\n" + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".pii_encryption_keys OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".pii_encryption_keys TO " + dbUser);
        query.executeUpdate();
    }

    public void createProjectSecurityGroupTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".project_security_group (\n"
              + "\tid serial NOT NULL,\n" + "\tproject_id int4 NOT NULL,\n"
              + "\tdeployment_profile_id int4 NULL,\n" + "\tsecurity_group_id int4 NOT NULL,\n"
              + "\tadded timestamptz NOT NULL DEFAULT now(),\n"
              + "\tlast_modified timestamptz NOT NULL DEFAULT now(),\n"
              + "\tCONSTRAINT project_security_group_pkey PRIMARY KEY (id),\n"
              + "\tCONSTRAINT unique_mapping UNIQUE (project_id, deployment_profile_id),\n"
              + "\tCONSTRAINT fk_psg_deployment_profile FOREIGN KEY (deployment_profile_id) REFERENCES "+customerId+".deployment_profiles(id),\n"
              + "\tCONSTRAINT project_security_group_project_id_foreign FOREIGN KEY (project_id) REFERENCES "+customerId+".client_hardeninig_project(client_hardeninig_project_id),\n"
              + "\tCONSTRAINT project_security_group_security_group_id_foreign FOREIGN KEY (security_group_id) REFERENCES "+customerId+".security_groups(id)\n"
              + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".project_security_group OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".project_security_group TO " + dbUser);
        query.executeUpdate();
    }

    public void createReportStatusTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".report_status (\n"
              + "\treport_id serial NOT NULL,\n" + "\tjob_id text NOT NULL,\n"
              + "\treport_type text NOT NULL,\n" + "\treport_format text NOT NULL,\n"
              + "\tstatus int4 NOT NULL,\n" + "\tstart_date timestamptz NULL,\n"
              + "\tcompletion_date timestamptz NULL,\n" + "\t\"version\" int4 NOT NULL,\n"
              + "\tfilename text NULL,\n" + "\trequest json NULL,\n"
              + "\tCONSTRAINT report_status_pkey PRIMARY KEY (report_id, job_id, report_type, report_format, version)\n"
              + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager
            .createNativeQuery("ALTER TABLE " + customerId + ".report_status OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager
            .createNativeQuery("GRANT ALL ON TABLE " + customerId + ".report_status TO " + dbUser);
        query.executeUpdate();
    }

    public void createScanInstanceTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".scan_instance (\n"
              + "\tid serial NOT NULL,\n" + "\tunique_id text NULL,\n"
              + "\tcreated timestamp NOT NULL DEFAULT now(),\n" + "\tcompleted timestamp NULL,\n"
              + "\tscan_completed timestamp NULL,\n" + "\terror text NULL,\n"
              + "\tstate int4 NULL,\n" + "\tscandata_id int4 NOT NULL,\n"
              + "\tCONSTRAINT scan_instance_pkey PRIMARY KEY (id),\n"
              + "\tCONSTRAINT scan_instance_scandata_id_foreign FOREIGN KEY (scandata_id) REFERENCES "+customerId+".scandata(scandata_id)\n"
              + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Index
        query = entityManager.createNativeQuery(
            "CREATE INDEX scan_instance_scandata_id_index ON " + customerId
                + ".scan_instance USING btree (scandata_id);");
        query.executeUpdate();

        //Permissions
        query = entityManager
            .createNativeQuery("ALTER TABLE " + customerId + ".scan_instance OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager
            .createNativeQuery("GRANT ALL ON TABLE " + customerId + ".scan_instance TO " + dbUser);
        query.executeUpdate();
    }

    public void createScanPolicyPreferencesTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
            "CREATE TABLE " + customerId + ".scan_policy_preferences (\n"
                + "\tid serial NOT NULL,\n" + "\tscandata_id int4 NOT NULL,\n"
                + "\tcreated timestamp NOT NULL DEFAULT now(),\n"
                + "\tenforcement_type text NOT NULL DEFAULT 'report-only',\n"
                + "\tblock_mixed_content bool NULL DEFAULT true,\n"
                + "\tframe_ancestors text NULL,\n"
                + "\treport_uri text NULL DEFAULT 'tala-cloud',\n"
                + "\tcustom_policy_directives json NULL,\n"
                + "\tsri_enabled bool NOT NULL DEFAULT false,\n" + "\tsri_scripts json NULL,\n"
                + "\tsri_updated timestamptz NULL,\n"
                + "\trewrite_problematic_constructs bool NOT NULL DEFAULT false,\n"
                + "\tsri_fallback_enabled bool NOT NULL DEFAULT false,\n"
                + "\tsri_reporting_enabled bool NOT NULL DEFAULT false,\n"
                + "\tenable_nonce bool NOT NULL DEFAULT false,\n"
                + "\treferrer_header_values json NULL,\n" + "\tcustom_domains json NULL,\n"
                + "\timg_src_wildcard json NULL DEFAULT '[\"https:\",\"http:\"]',\n"
                + "\trule_id int4 NULL,\n" + "\tfeature_policy json NULL,\n"
                + "\tiframe_sandboxing json NULL,\n"
                + "\tCONSTRAINT scan_policy_preferences_enforcement_type_check CHECK ((enforcement_type = ANY (ARRAY['report-only', 'block']))),\n"
                + "\tCONSTRAINT scan_policy_preferences_pkey PRIMARY KEY (id),\n"
                + "\tCONSTRAINT fk_rule_id FOREIGN KEY (rule_id) REFERENCES " + customerId
                + ".automark_fp_rules(id),\n"
                + "\tCONSTRAINT scan_policy_preferences_scandata_id_foreign FOREIGN KEY (scandata_id) REFERENCES "
                + customerId + ".scandata(scandata_id)\n" + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Index
        query = entityManager.createNativeQuery(
            "CREATE INDEX scan_policy_preferences_scandata_id_index ON " + customerId
                + ".scan_policy_preferences USING btree (scandata_id);");
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".scan_policy_preferences OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".scan_policy_preferences TO " + dbUser);
        query.executeUpdate();
    }

    public void createScanDataTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".scandata (\n"
              + "\tscandata_id serial NOT NULL,\n" + "\tunique_id text NULL,\n"
              + "\tcallback text NULL,\n" + "\tcreated timestamp NOT NULL DEFAULT now(),\n"
              + "\tcompleted timestamp NULL,\n" + "\t\"depth\" int4 NULL,\n"
              + "\terror text NULL,\n" + "\tmax_pages int4 NULL,\n" + "\tstate int4 NULL,\n"
              + "\t\"name\" text NULL,\n" + "\tclient_hardeninig_project_id int4 NOT NULL,\n"
              + "\tautomation_mode text NOT NULL DEFAULT 'manual',\n"
              + "\tschedule_start_date timestamp NULL,\n" + "\tschedule_end_date timestamp NULL,\n"
              + "\tschedule_frequency int4 NULL,\n" + "\tschedule_days_of_week text NULL,\n"
              + "\tauthentication_parameters json NULL,\n" + "\tclickables json NULL,\n"
              + "\tseed_urls json NULL,\n" + "\tadd_seed_urls bool NULL DEFAULT true,\n"
              + "\tseed_url_file bytea NULL,\n" + "\tseed_url_filename text NULL,\n"
              + "\tpii_pages json NULL,\n" + "\tapplication_roots json NULL,\n"
              + "\tcustom_headers json NULL,\n" + "\tsynthentic_transaction_ids json NULL,\n"
              + "\tincremental_scan bool NOT NULL DEFAULT false,\n"
              + "\tCONSTRAINT scandata_automation_mode_check CHECK ((automation_mode = ANY (ARRAY['manual', 'triggered', 'scheduled']))),\n"
              + "\tCONSTRAINT scandata_pkey PRIMARY KEY (scandata_id),\n"
              + "\tCONSTRAINT scandata_client_hardeninig_project_id_foreign FOREIGN KEY (client_hardeninig_project_id) REFERENCES "+customerId+".client_hardeninig_project(client_hardeninig_project_id)\n"
              + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Index
        query = entityManager.createNativeQuery(
            "CREATE INDEX scandata_client_hardeninig_project_id_index ON " + customerId
                + ".scandata USING btree (client_hardeninig_project_id);");

        query.executeUpdate();

        //Permissions
        query = entityManager
            .createNativeQuery("ALTER TABLE " + customerId + ".scandata OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager
            .createNativeQuery("GRANT ALL ON TABLE " + customerId + ".scandata TO " + dbUser);
        query.executeUpdate();
    }

    public void createScanScopeTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".scanscope (\n"
              + "\tscanscope_id serial NOT NULL,\n" + "\tregex text NULL,\n"
              + "\tpositive int4 NULL,\n" + "\tscandata_id int4 NOT NULL,\n"
              + "\tCONSTRAINT scanscope_pkey PRIMARY KEY (scanscope_id),\n"
              + "\tCONSTRAINT scanscope_scandata_id_foreign FOREIGN KEY (scandata_id) REFERENCES "+customerId+".scandata(scandata_id)\n"
              + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Index
        query = entityManager.createNativeQuery(
            "CREATE INDEX scanscope_scandata_id_index ON " + customerId
                + ".scanscope USING btree (scandata_id)");

        query.executeUpdate();

        //Permissions
        query = entityManager
            .createNativeQuery("ALTER TABLE " + customerId + ".scanscope OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager
            .createNativeQuery("GRANT ALL ON TABLE " + customerId + ".scanscope TO " + dbUser);
        query.executeUpdate();
    }

    public void createScriptLogTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".script_log (\n"
              + "\tproject_id int4 NOT NULL,\n" + "\t\"path\" text NULL,\n"
              + "\tmb_limit int4 NULL DEFAULT 0\n" + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager
            .createNativeQuery("ALTER TABLE " + customerId + ".script_log OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager
            .createNativeQuery("GRANT ALL ON TABLE " + customerId + ".script_log TO " + dbUser);
        query.executeUpdate();
    }

    public void createSecurityGroupsTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".security_groups (\n"
              + "\tid serial NOT NULL,\n" + "\t\"name\" text NULL,\n"
              + "\ttls_enabled bool NULL DEFAULT false,\n"
              + "\thmac_enabled bool NULL DEFAULT false,\n"
              + "\tadded timestamptz NOT NULL DEFAULT now(),\n"
              + "\tlast_modified timestamptz NOT NULL DEFAULT now(),\n"
              + "\tCONSTRAINT security_groups_pkey PRIMARY KEY (id),\n"
              + "\tCONSTRAINT unique_group_name UNIQUE (name)\n" + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager
            .createNativeQuery("ALTER TABLE " + customerId + ".security_groups OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".security_groups TO " + dbUser);
        query.executeUpdate();
    }

    public void createSecuritySettingsTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
            "CREATE TABLE " + customerId + ".security_settings (\n"
                + "\ttls_enabled bool NULL DEFAULT false,\n"
                + "\thmac_enabled bool NULL DEFAULT false,\n" + "\tcmk_key_id text NULL\n" + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".security_settings OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".security_settings TO " + dbUser);
        query.executeUpdate();
    }

    public void createSyntheticTransactionsTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
            "CREATE TABLE " + customerId + ".synthentic_transactions (\n"
                + "\tfile_id serial NOT NULL,\n" + "\tfile_name text NULL,\n"
                + "\tfile bytea NULL,\n"
                + "\tCONSTRAINT synthentic_transactions_pkey PRIMARY KEY (file_id)\n" + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager.createNativeQuery(
            "ALTER TABLE " + customerId + ".synthentic_transactions OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".synthentic_transactions TO " + dbUser);
        query.executeUpdate();
    }

    public void createUserActionLogsTable(String customerId) {
        Query query = entityManager.createNativeQuery(
            //@formatter:off
          "CREATE TABLE "+customerId+".user_action_log (\n"
              + "\tid serial NOT NULL,\n" + "\tuser_id int4 NOT NULL,\n"
              + "\t\"source\" text NOT NULL,\n" + "\t\"action\" text NOT NULL,\n"
              + "\t\"timestamp\" timestamptz NOT NULL DEFAULT now(),\n"
              + "\tadditional_details json NULL,\n"
              + "\tCONSTRAINT user_action_log_pkey PRIMARY KEY (id)\n" + ")"
            //@formatter:on
        );
        query.executeUpdate();

        //Permissions
        query = entityManager
            .createNativeQuery("ALTER TABLE " + customerId + ".user_action_log OWNER TO " + dbUser);
        query.executeUpdate();

        query = entityManager.createNativeQuery(
            "GRANT ALL ON TABLE " + customerId + ".user_action_log TO " + dbUser);
        query.executeUpdate();
    }
}
