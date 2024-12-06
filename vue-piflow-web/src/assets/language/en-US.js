module.exports = {
    title: window.$SYSTEM_TITLE_EN,
    logOut: 'Log out',
    sidebar: {
        dashboard: "Dashboard",
        flow: "Flow",
        group: "Group",
        processes: "Process",
        template: "Template",
        data_source: "DataSource",
        schedule: "Schedule",
        stopHub: "StopHub",
        sparkJar: "SparkJar",
        testData: "TestData",
        code: "Code",
        publish: "Publish",
        example: "Example",
        admin: "Admin",
        admin_schedule: 'Schedule',
        stopsComponent: 'StopsComponent',
        globalVariable: 'GlobalVariable',
        user:"User",
        myCenter:'My Center',
        log:"Log",
        modification:"Modification",
        bindingAccount:"BindingAccount",
        basicInfo:"Basic Information",
        LocalDataSource:"Local Data",
        LocalModelAlgorithm:"Local Model",
        Example:"Flow Sample",
        admin:"Admin",
        LabelManagement:"Label Management",
        selectSource:"Select Label",
        pythonMirror:"Image Management",
        visualization: "Visualization",
        FlowSchedule:"Flow Schedule",
        FileSchedule:"File Schedule",
        TimingSchedule:"Timing Schedule",
        database:'MySQLConfig',
        datasource:'VisualizationSource',
        visualconfig:'Visualization',
    },
    page: {
        prev_text: "Previous",
        next_text: "Next"
    },
    index: {
        cpu: "Cores",
        loading: "Loading..."
    },
    tip: {
        title: "πFlow system tips",
        existed: "UserName Existed！",
        fault_content: "An unknown error occurred in the background run！",
        update_success_content: "Update succeeded！",
        update_fail_content: "Update failed or admin information cannot be changed！",
        save_success_content: "Template saved successfully！",
        save_fail_content: "Template save failed！",
        add_success_content: "Added succeeded！",
        add_fail_content: "Add failed！",
        run_success_content: "Run succeeded！",
        run_fail_content: "Run failed！",
        mount_success_content: "Mount succeeded！",
        mount_fail_content: "Mount failed！",
        stop_success_content: "Stop succeeded！",
        stop_fail_content: "Stop failed！",
        unmount_success_content: "Unmount succeeded！",
        unmount_fail_content: "Unmount failed！",
        get_success_content: "Data acquisition successful！",
        get_fail_content: "Data acquisition failed！",
        get_user_content:"User does not have this permission!",
        debug_success_content: "Debug succeeded！",
        debug_fail_content: "Debug failed！",
        delete_success_content: "Deleted successfully！",
        delete_fail_content: "Deletion failed！",
        request_fail_content: "Data request failed！",
        reference_content: "Referenced, not allowed to delete!",

        upload_success_content: "Template upload successful！",
        upload_fail_content: "Template upload failed！",
        download_success_content: "Download successful！",
        download_fail_content: "Download failed！",

        data_fail_content: "Data acquisition failed！",
    },
    modal: {
        placeholder: "Please enter…",
        template_title: "Template name",
        ok_text: "Submit",
        upload_text: "upload",
        confirm: "Confirm",
        cancel_text: "Cancel",
        reset_text: "Reset",
        delete_content: "Are you sure to delete",
        cancel_content: "Are you sure you want to cancel this creation",
        editSave:"Edit or save",
        next:"Next"
    },
    flow_columns: {
        create_title: "Create Flow",
        update_title: "Update Flow",
        flow_name: "FlowName",
        driverMemory: "DriverMemory",
        executorNumber: "ExecutorNumber",
        executorMemory: "ExecutorMemory",
        executorCores: "ExecutorCores",
        name: "Name",
        description: "Description",
        CreateTime: "CreateTime",
        action: "Actions"
    },
    group_columns: {
        create_title: "Create Group",
        update_title: "Update Group",
        group_name: "GroupName",
        name: "Name",
        description: "Description",
        CreateTime: "CreateTime",
        action: "Actions"
    },
    progress_columns: {
        progress: "Progress",
        endTime: "EndTime",
        startTime: "StartTime",
        processType: "ProcessType",
        description: "Description",
        id: "ProcessGroupId",
        name: "Name",
        state: "State",
        action: "Actions"
    },
    template_columns: {
        name: "Name",
        crtDttm: "CreateTime",
        action: "Actions"
    },
    dataSource_columns: {
        create_title: "Create DataSource",
        update_title: "Update DataSource",
        type: "Type",
        dataSource_name: "Name",
        addProperty: "AddProperty",
        name: "Name",
        description: "Description",
        createTime: "CreateTime",
        lastUpdateTime:"LastUpdateTime",
        dataSourceType: "DataSourceType",
        action: "Actions",
        isLinked: "Do you synchronize the changes? Related components are：",
        yes: "Yes",
        no: "No",
    },
    schedule_columns: {
        create_title: "Create Schedule",
        update_title: "Update Schedule",
        scheduleType: "Schedule Type",
        startDate: "StartDate",
        endDate: "EndDate",
        flowIsGroup: "Flow / Group",
        cron: "Cron",
        type:'Type',
        name: "Name",
        StartTime:"StartTime",
        EndTime:"EndTime",
        status: "Status",
        action: "Actions"
    },
    StopHub_columns: {
        upload: "Upload Jar",
        name: "Name",
        time:"Time",
        version: "Version",
        jarUrl: "JarUrl",
        status: "Status",
        action: "Actions",
        jarDescription: "Click or drag jar here to upload"
    },
    testData_columns:{
        create_title: "Create TestData",
        update_title: "Update TestData",
        testData_name: "TestDataName",
        name: "Name",
        description: "Description",
        methed: "Methed",
        header: "Header",
        delimiter: "Delimiter",
        schema: "Schema",
        CreateTime: "CreateTime",
        action: "Actions",
        fileDescription: "Click or drag file here to upload",
        uploadPrompt: "If the CSV file you upload contains Chinese characters, please use UTF-8 encoding"
    },
    code_columns: {
        create_title: "Create Code",
        update_title: "Update Code",
        code_name: "CodeName",
        name: "Name",
        description: "Description",
        CreateTime: "CreateTime",
        UpdateTime: "UpdateTime",
        action: "Actions"
    },
    admin_columns: {
        create_title: "Create Schedule",
        update_title: "Update Schedule",
        jobName: "Name",
        jobClass: "Class",
        cronExpression: "Cron",
        name: "Name",
        class: "Class",
        cron: "Cron",
        status: "Status",
        createTime: "CreateTime",
        action: "Actions"
    },
    user_columns:{
        create_title: "Create User",
        update_title: "Update User",
        name: "Name",
        username: "UserName",
        status: "Status",
        createTime: "CreateTime",
        action: "Actions",
        password:"Password",
        role:"Role",
        passwordComplexity:'The password must contain uppercase and lowercase digits and characters and must be longer than 8 characters',
        passwordDiff:'The new password cannot be the same as the old password!',
        newPsd:"Please enter your new password",
        newPsdCheck:"Please enter your new password again",
        oldPsd:"Please enter your old password",
        psdCheckDiff:"The two input passwords do not match",
        assign_roles:"Assign roles",
        role:'Role'
    },
    modification_columns:{
        oldPasswd: "Old Password",
        passwd: "New Password",
        passwdCheck: "Confirm",
        Reset: "Reset",
        Submit: "Submit"
    },
    bindingAccount_columns:{
        value: "Account",
        Reset: "Reset",
        Submit: "Submit"
    },
    flow_schedule:{
        create_title: "Create Schedule",
        update_title: "Update Schedule",
        scheduleType: "Schedule Type",
        startDate: "StartDate",
        endDate: "EndDate",
        flowIsGroup: "Flow",
        name:'Name',
        type:'Type',
        cron:'Cron',
        file:"File",
        path:"File Path",
        prefix:"Prefix Matching",
        suffix:"Suffix Matching",
        startTime:'StartTime',
        endTime:'EndTime',
        status:'Status',
        description: "Description",
        trigger:'Trigger',
        parallel:'Parallel',
        serial:'Serial',
        serialRule:'Serial Rule',
        fileUpdata:'Last updated time of the file',
        fileName:'File Name',
        serialOrder:'Serial Order',
        ascendingOrder:'Ascending Order',
        descendingOrder:'Descending Order',
        regex:'Regex',
        action:'Actions',
    },
    log_columns:{
        username: "UserName",
        lastLoginIp:"Ip",
        createTime: "CreateTime",
        lastUpdateTime:"LastUpdateTime",
        action: "Actions"
    },
    stopsComponent_columns: {
        name: "Name",
        description: "Description",
    },
    globalVariable_columns: {
        create_title: "Create GlobalVariable",
        update_title: "Update GlobalVariable",
        content: "Value",
        name: "Name",
        type: "Type",
        createTime: "CreateTime",
        action: "Actions"
    },
    homeInfo: {
        introduction_title: "Introduction",
        monitor_title: "Monitor",
        statistics_title: "Statistics",
        CPU_Disk: 'CPU Disk Usage',
        Memory_Disk: 'Memory Disk Usage',
        HDFS_Disk: 'HDFS Disk Usage',
        totalCapacity: 'Total Capacity',
        Used: 'Used',
        introduction_Info:window.$SYSTEM_INTRODUCTION_INFO_EN,
        flowStatistics: "Pipeline statistics, include the number of pipeline flows, the number of processors in the running state, and the number of processors in each running state.",
        scheduleStatistics: "Scheduling statistics, include the number of scheduling pipelines/pipeline groups, and the number of schedules in each state.",
        groupStatistics: "Pipeline group statistics, include the number of pipeline groups, the number of processors of the pipeline groups in the running state, and the number of processors in each running state.",
        OtherStatistics: "Other statistics include the number of DataSources, the number of custom component plug-ins StopsHub, and the number of templates.",
        ComponentStatistics: "Component statistics, include the number of data processing components Stop and the number of data processing component groups StopGroup.",
    },
    python:{
        packageName:"ComponentPackageName",
        version:"Version",
        FileUrl:"FileUrl",
        state:"State",
        componentName:"Component name",
        viewDetail:"View details",
        noData:"NoData",
        detailInfo:"Detailed information",
        pythonComponent:"Python component：",
        pythonVersion:"Python version：",
        parameter:"Whether a parameter is passed",
        uploadLogo:"Upload icon",
        expandInfo:"Expand info",
        logo:"logo",
        team:"Team",
        componentClass:"Component class",
        chineseName:"Chinese name",
        Instructions:"Instructions",
        description:"Description",
        author:"Author",
        email:"Email",
        algorithm:"Collaborative model algorithm",
        added:"Added",
        unadded:"Unadded",
        upload: 'Upload',
        language: 'Language',
        version_lang: 'Version',
        stopsHubName: 'StopsHubName',
        uploadZip:'Upload .zip',
        uploadZipJar:'Upload .jar or .zip',
        InteractiveProgramming:'Interactive programming',
        base_image:"Base Image",
        image:"Algorithm Image",
        image_description:"Image description",
    },
    python_mirror:{
        create_title: "Create Docker Image",
        update_title: "Update Docker Image",
        createTime: "CreateTime",
        name:"Name",
        version:"Version",
        description:"Description",
        harborUser:"User Name",
        password:"Password",
        action:'Actions',
    },
    basicInfo:{
        basicInfo:"Basic Information",
        dataCenterName:"Data center name:",
        email:"Contact Email:",
        org:'Supporting Institution:',
        province:'Province / City:',
        address:'Platform Website:',
        desc:"Description:",
        tel:'Contact Number:',
        info:'Attachment:',
        uploadImg:'Upload Image:',
        save:"Save",
        department:'Administration:'
    },
    database:{
        create_title: "MySQL Connection",
        update_title: "MySQL Connection",
        name:'Name',
        description: "Description",
        url: "Url",
        driver: "Driver",
        user: "User",
        password: "Password",
        createTime: "CreateTime",
        action: "Actions"
    },
    datasource:{
        create_title: "Visualization Source Configuration",
        update_title: "Visualization Source Configuration",
        database:'MySQL',
        datasheet:'Table',
        sheetName:'Sheet Name',
        excelName:'Excel Name',
        sheetNamePlaceholder:'Default selection is the first Sheet',
        type:'Type',
        upload:'Upload Excel',
        name:'Name',
        description: "Description",
        createTime: "CreateTime",
        action: "Actions"
    },
    visualconfig:{
        create_title: "Create VisualConfig",
        update_title: "Update VisualConfig",
        datasource:'Visualization Source',
        name:'Name',
        description: "Description",
        createTime: "CreateTime",
        action: "Actions"
    },
}