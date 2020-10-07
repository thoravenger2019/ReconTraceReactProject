import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import axios, { axiosGet } from '../utils/axios';
import MenuSideBar from './menuSideBar';
import {
  Form,
  Input,
  Button,
  Select,
  Card,
  Row,
  Col,
  Checkbox,
  Layout,
  Avatar,
  TimePicker,
  Table,
  Space,
} from 'antd';
import Password from 'antd/lib/input/Password';
import { LayoutContext } from 'antd/lib/layout/layout';
import Title from 'antd/lib/typography/Title';
import moment from 'moment';
import Grid from "@material-ui/core/Grid";
const { Header, Footer, Sider, Content } = Layout;
const FormItem = Form.Item;
const { Option } = Select;

const Demo = props => {
  console.log(props)
  const [ClientData, setClientData] = useState([])
  const [logData, setLogData] = useState([])
  const [P_VENDORTYPE, setLogType] = useState([])
  const [modeData, setModeData] = useState([])
  const [P_CLIENTID, setClientID] = useState([])
  const [vendorData, setVendorData] = useState([])
  const [ChannelData, setChannelData] = useState([])
  const [P_CHANNELID,setChannelID]=useState([])
  const [P_MODEID,setModelID]=useState([])
  const [P_FILEXML,setFileTypeID]=useState([])
  const [fileExt,setFileExt]=useState([])
  const [P_VENDORID,setVendorID]=useState([])
  const [cname,setClientName]=useState([])
  const [fid,setFormatID]=useState([])
  const [ConfigTableData,setConfigTableData]=useState([])
  const [inputFilePre,setInputFilePre]=useState([])
  const [inputCutOff,setInputCutOff]=useState([])
  const [fileloader, setFilePlaintextLoader] = useState(false)
  const [filetype, setFileLoader] = useState(false)
  const [channelloader, setChannelLoader] = useState(false)
  const [separatorloader, setSeperator] = useState(false)
  const [loader, setLoader] = useState(false)
  const [configLoader,setConfigTable]=useState(false)
  const [clientInfoCard,setClientInfoCard]=useState(false)
  console.log(P_FILEXML)
  console.log(fileExt)

  useEffect(() => {
    onDisplayClientNameList();
    onDisplayChannel();
  }, [])


  const format = 'HH:mm';
  const onDisplayClientNameList = async () => {
    try {
      const clientNameResponse = await axios.get(`clientName`);
      console.log(clientNameResponse.data)
      setLoader(false);

      const clientNameN = clientNameResponse.data;
      console.log(clientNameN);
      const clientNameList = clientNameN.map((item, index) =>
        <Option value={item.id} key={index}>{item.clientNameList}
        </Option>
      )
      setClientData(clientNameList);

    } catch (e) {
      console.log(e)
    }
  };
  const onDisplayLogFileList = async (value) => {
    try {
      const logNameResponse = await axios.get(`getLogList/${value}`);
      console.log(logNameResponse.data)
      setLoader(false);

      const logNameN = logNameResponse.data;
      console.log(logNameN)
      const clientNameList = logNameN.map((item, index) =>
        <Option value={item.ID} key={index}>{item.LogType}
        </Option>
      )
      setLogData(clientNameList);

    } catch (e) {
      console.log(e)
    }
  };
  const onDisplayChannelList = async (P_CLIENTID) => {
    try {
      console.log(P_CLIENTID)
      const channelResponse = await axios.get(`getChannelID`);
          console.log(channelResponse.data)
          setLoader(false);

          const channelN = channelResponse.data;
          console.log(channelN);
     
          const listChannel=channelN.map((item,index)=> <Option value={item.roleID} key={index} label={item.channeltype}>{item.channeltype}</Option>)
          setChannelData(listChannel);
              
     
    } catch (e) {
      console.log(e)
    }
  };


  const onDisplayModeType = async (P_CLIENTID, value) => {
    try {
      const modeTypeResponse = await axios.get(`getModeType/${P_CLIENTID}/${value}`);
      console.log(modeTypeResponse.data)
      //setLoader(false);
      const modeTypeN = modeTypeResponse.data;
      console.log(modeTypeN);
      const listmodeType = modeTypeN.map((item, index) => <Option value={item.modeid} key={index} label={item.modename}>{item.modename}</Option>)
      setModeData(listmodeType);
    } catch (e) {
      console.log(e)
    }
  };

  const onDisplayVendor = async (checkedValues) => {
    try {
      const vendorResponse = await axios.get(`getVendorDetailsByType/${checkedValues}`);
      console.log(vendorResponse.data)
      setLoader(false);

      const vendorN = vendorResponse.data;

      const listVendor = vendorN.map((item, index) => <Option value={item.VendorID} key={index}>{item.VendorName}</Option>)
      setVendorData(listVendor);

    } catch (e) {
      console.log(e)
    }
  };
  const onDisplayChannel = async () => {
    try {
      const channelResponse = await axios.get(`getChannelID`);
      console.log(channelResponse.data)
      setLoader(false);

      const channelN = channelResponse.data;
      console.log(channelN);

      const listChannel = channelN.map((item, index) => <Option value={item.roleID} key={index} label={item.channeltype}>{item.channeltype}</Option>)
      setChannelData(listChannel);

    } catch (e) {
      console.log(e)
    }
  };

  const onDisplayConfigTable = async (value) => {
    try {
      const configTableResponse = await axios.get(`getfileformatclient/${value}`);
      console.log(configTableResponse.data)
      setLoader(false);

      const ConfigTableN = configTableResponse.data;
      console.log(ConfigTableN);

      const listConfigTable = ConfigTableN.map((item, index) => ({
        ClientName: item.clientName,
        LogType: item.vendorType,
        Channel: item.channelName,
        FilePrefix: item.filePrefix,
        Mode: item.transactionMode,
        VendorName: item.vendorName,
        FileExtention: item.fileExtension,
        key: index
      })
      )
     
      setConfigTableData(listConfigTable);
    } catch (e) {
      console.log(e)
    }
  };

  const menuData = props.location.state;
  console.log(menuData);

  const onFilePre = event =>{
    setInputFilePre(event.target.value);
  }
  const onCutOff = event =>{
    setInputCutOff(event.target.time);
  }
  
  function onChangeClientName(value) {
    console.log(`selected ${value}`);
    onDisplayLogFileList(value);
    onDisplayConfigTable(value);
    setClientID(value);
    setConfigTable(true)
  }
  function onChangeLog(checkedValues) {
    console.log('checked = ', checkedValues);
    setLogType(checkedValues)
    onDisplayVendor(checkedValues);
    onDisplayChannelList(P_CLIENTID);
    setChannelLoader(true)

  }
  function onChangeChannel(value) {
    console.log(`selected ${value}`);
    setChannelID(value)
    onDisplayModeType(P_CLIENTID, value)
  }

  function onChangeFileType(checkedValues) {
    console.log('checked = ', checkedValues);
    setFileTypeID(checkedValues)
    
    if (checkedValues == 'spreadsheet') {
      setFileTypeID(checkedValues)
      setFileLoader(true);
      setFilePlaintextLoader(false);
      setSeperator(false)

    }
    else if (checkedValues == 'plaintext') {
      setFilePlaintextLoader(true);
      setFileLoader(false);
      setSeperator(false)
    }
    if (checkedValues == 'plaintextwithSeparator') {
      setFilePlaintextLoader(false);
      setFileLoader(false);
      setSeperator(true)
    }

  }
  
  function onChangeMode(checkedValues) {
    console.log('checked = ', checkedValues);
    setModelID(checkedValues)
  }
  
 

  function onChangeVendor(checkedValues) {
    console.log('checked = ', checkedValues);
    setVendorID(checkedValues);
    getFileFormatHistory(P_VENDORTYPE,P_CLIENTID,P_CHANNELID,P_MODEID,checkedValues);
    setClientInfoCard(true);
  }
  function onChange(checkedValues) {
    console.log('checked = ', checkedValues);
  }
  function onChangeExt(checkedValues) {
    console.log('checked = ', checkedValues);
    setFileExt(checkedValues)
  }
  
  function handleChange(value) {
    console.log(`selected ${value}`);
  }

  const [form] = Form.useForm()
  
const getFileFormatHistory = async (P_VENDORTYPE,P_CLIENTID,P_CHANNELID,P_MODEID,P_VENDORID) => {
    try {
       //console.log(inputFilePre);
      
       console.log(P_VENDORID)
       const response = await axios.get(`getFileFormatHistory/${P_VENDORTYPE}/${P_CLIENTID}/${P_CHANNELID}/${P_MODEID}/${P_VENDORID}`);
       console.log(response.data)
       const formatHistoryResponse=response.data;
       const clientNameSet = formatHistoryResponse.map(item =>  item.ClientName );
       const FormatIDSet=formatHistoryResponse.map(item=>item.FormatID);
 
       setClientName(clientNameSet);
       setFormatID(FormatIDSet);
        /*
       if(JSON.stringify(response.data) === 'Save')
       {
         alert("user added successfully");
       }
       else{9
         alert("already exist");
       }
        //props.history.push("/AddUser",response.data)
  */ 
    } catch (e) {
      console.log(e)
    }
  }


  const onFileConfig = async () => {
    try {

      const validateFields = await form.validateFields();    
       const values = form.getFieldsValue();
       console.log(values)
       const filePrefix=inputFilePre;
       const formDataFilePre=new FormData();
       formDataFilePre.append('P_FILEPREFIX',filePrefix);

       const cutOff=inputCutOff;
       const formDataCutOff=new FormData();
       formDataCutOff.append('P_CUTOFFTIME',cutOff);
       
       const abc=fileExt.split('.');
       const P_FILEEXT=abc[0];
   // console.log(P_FILEEXT[1]);
       const response = await axios.post(`getinsertfileformat/${P_CLIENTID}/${P_VENDORTYPE}/${P_CHANNELID}/${P_MODEID}/${P_FILEXML}/${values.P_FILEEXT}/${P_VENDORID}`,formDataFilePre,formDataCutOff);
       console.log(response.data)
        /*
       if(JSON.stringify(response.data) === 'Save')
       {
         alert("user added successfully");
       }
       else{9
         alert("already exist");
       }
        //props.history.push("/AddUser",response.data)
  */ 
    } catch (e) {
      console.log(e)
    }
  }



  const [componentSize, setComponentSize] = useState('small');

  const onFormLayoutChange = ({ size }) => {
    setComponentSize(size);
  };
  const tailLayout = {
    wrapperCol: { offset: 10 },
  };
  const FormItem = Form.Item;

  const onReset = () => {
    form.resetFields();
    setConfigTable(false);
    setClientInfoCard(false);
  };

  const columnsConfigFormat = [{
    title: 'ClientName',
    dataIndex: 'ClientName',
    key: 'ClientName',
    render: text => <a>{text}</a>,
  },
  {
    title: 'LogType',
    dataIndex: 'LogType',
    key: 'LogType',
    /*render: (roleID,record) => (
      <Space size="middle">
        <a onClick={() => { onAccess(record) }}><UserSwitchOutlined /></a>
        {console.log(record)}
      </Space>
    ),*/
  },
  {
    title: 'Channel',
    dataIndex: 'Channel',
    key: 'Channel',
    /*render: (text, record) => (
      <Space size="middle">
        <a><EditOutlined /></a>
      </Space>
    ),*/
  },
  {
    title: 'FilePrefix',
    dataIndex: 'FilePrefix',
    key: 'FilePrefix',

  },
  {
    title: 'Mode',
    dataIndex: 'Mode',
    key: 'Mode',

  },
  {
    title: 'VendorName',
    dataIndex: 'VendorName',
    key: 'VendorName',

  },
  {
    title: 'FileExtention',
    dataIndex: 'FileExtention',
    key: 'FileExtention',

  }];

  const columns = [{
    title: 'Field Name',
    dataIndex: 'FieldName',
    key: 'FieldName',
    render: text => <a>{text}</a>,
  },
  {
    title: 'Start Position',
    dataIndex: 'Start Position',
    key: 'Start Position',
    render: (text, record) => (
      <Space size="middle">
        <Input/>
      </Space>
    ),
  },
  {
    title: 'Field Length',
    dataIndex: 'Field Length',
    key: 'Field Length',
    render: (text, record) => (
      <Space size="middle">
        <Input/>
      </Space>
    ),
  }];
  const dataSource = [
  {
    key: 'FieldName ',
    FieldName: 'Amount1Prefix',
  },
  {
    key: 'FieldName',
    FieldName: 'Amount2Prefix',
  },
  {
    key: 'FieldName',
    FieldName: 'Amount3Prefix',
  },
  {
    key: 'FieldName',
    FieldName: 'ATMAccountNoPrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'ATMBalancePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'AuthCodePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'BranchCodePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'CardNumberPrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'CardTypePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'Cassette1Prefix',
  },
  {
    key: 'FieldName',
    FieldName: 'Cassette2Prefix',
  },
  {
    key: 'FieldName',
    FieldName: 'Cassette3Prefix',
  },
  {
    key: 'FieldName',
    FieldName: 'Cassette4Prefix',
  },
  {
    key: 'FieldName',
    FieldName: 'CurrencyCodePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'CustAccountNoPrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'CustBalancePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'DrCrTypePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'FeeAmountPrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'NoOfDuplicatePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'ProcessingCodePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'ReferenceNumberPrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'ReserveField1Prefix',
  },
  {
    key: 'FieldName',
    FieldName: 'ReserveField2Prefix',
  },
  {
    key: 'FieldName',
    FieldName: 'ReserveField3Prefix',
  },
  {
    key: 'FieldName',
    FieldName: 'ReserveField4Prefix',
  },
  {
    key: 'FieldName',
    FieldName: 'ReserveField5Prefix',
  },
  {
    key: 'FieldName',
    FieldName: 'ResponseCodePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'RevEntryLegPrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'ReversalFlagPrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'TerminalIdPrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'TxnsAmountPrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'TxnsDateTimePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'TxnsNumberPrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'TxnsPerticularsPrefix',
  },  
  {
    key: 'FieldName',
    FieldName: 'TxnsPostDateTimePrefix',
  },  
  {
    key: 'FieldName',
    FieldName: 'TxnsStatusPrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'TxnsSubTypePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'TxnsTypePrefix',
  },
  {
    key: 'FieldName',
    FieldName: 'TxnsValueDateTimePrefix',
  },
];

  return (

    <Layout>
      <Header style={{ padding: "20px" }}>
        <Avatar shape="square" style={{ float: "right" }} size="default" src="./max.png" />
        <Title
          level={3} style={{ color: "white" }}>Trace</Title>
      </Header>
      <Layout>
        <MenuSideBar menuData={menuData} />
        <Layout style={{ height: "100vh", backgroundColor: "white" }}>
          <Content>
            <Grid container spacing={24}>
                <Grid item md={3}>
                <Table columns={columns}  dataSource={dataSource}
                         
                         scroll={{ y: 100 }} 
                         bordered 
                       
                         />         
                </Grid>
                <Grid item md={3}>
                <Table columns={columns}  dataSource={dataSource}
                         pagination={false} 
                         scroll={{ y: 800 }}
                         bordered 
                       
                         />    
                </Grid>
            </Grid>         
          </Content>
        </Layout>
      </Layout>
    </Layout >
  );
};
export default Demo;