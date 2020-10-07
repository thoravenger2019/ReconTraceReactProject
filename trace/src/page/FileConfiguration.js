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
  InputNumber,
  Popconfirm,
} from 'antd';
import Password from 'antd/lib/input/Password';
import { LayoutContext } from 'antd/lib/layout/layout';
import Title from 'antd/lib/typography/Title';
import moment from 'moment';

const { Header, Footer, Sider, Content } = Layout;
const FormItem = Form.Item;
const { Option } = Select;

  


const FileConfiguration = props => {
 // console.log(props)
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
  const [xmltable,setXmlTable]=useState([])
  const [tblfieldNames,setFieldNames]=useState([])
  const [upFieldName,setFieldName]=useState([])
  const [upFieldLen,setFieldLength]=useState([])
  const [upFieldStartPos,setStartPosition]=useState([])

  const [fileloader, setFilePlaintextLoader] = useState(false)
  const [filetype, setFileLoader] = useState(false)
  const [channelloader, setChannelLoader] = useState(false)
  const [separatorloader, setSeperator] = useState(false)
  const [loader, setLoader] = useState(false)
  const [configLoader,setConfigTable]=useState(false)
  const [clientInfoCard,setClientInfoCard]=useState(false)


  const [editingKey, setEditingKey] = useState('');
  const isEditing = record => record.key === editingKey;
 // console.log(P_FILEXML)
 // console.log(fileExt)
 
const EditableCell = ({
  editing,
  dataIndex,
  title,
  inputType,
  record,
  index,
  children,
  ...restProps
}) => {
  const inputNode = dataIndex === 'StartPosition' ? <Input value={upFieldStartPos}/> : <Input value={upFieldLen}/>;
  return (
    <td {...restProps}>
      {editing ? (
        <Form.Item
          name={dataIndex}
          style={{
            margin: 0,
          }}
          rules={[
            {
              required: true,
              message: `Please Input ${title}!`,
            },
          ]}
        >
          {inputNode}
        </Form.Item>
      ) : (
        children
      )}
    </td>
  );
};
const edit = (record) => {
  form.setFieldsValue({
    StartPosition: '',
    FieldLength: '',
    ...record,
  });
  setEditingKey(record.key);
};


const cancel = () => {
  setEditingKey('');
};


 useEffect(() => {
    onDisplayClientNameList();
    onDisplayChannel();
    //onTestXml();
  }, [])


  const format = 'HH:mm';
  
  const onTestXml = async () => {
    try {

      const xmlResponse = await axios.get(`xmlTojson`);
      console.log(xmlResponse.data);
      //setLoader(false);

     const xmlN = xmlResponse.data;
     console.log(xmlN);
     
     var nodeNameTest=xmlN.map(item=>item.NodeName);
     var filedNames = [];               
     for(var i in nodeNameTest) 
      filedNames.push(nodeNameTest[i]); 
         console.log(filedNames);
         alert("filedNames len"+filedNames.length);
         alert("filedNames[1]"+filedNames[1]);

     var startPosNodeValueNodeTest=xmlN.map(item=>item.startPosNodeValueNode);
     var posValue = []; 
      for(var i in startPosNodeValueNodeTest) 
        posValue.push(startPosNodeValueNodeTest[i]); 
        alert(startPosNodeValueNodeTest);

     var LengthNodeValueNode=xmlN.map(item=>item.LengthNodeValueNode);
     var lenValue = []; 
      for(var i in LengthNodeValueNode) 
      lenValue.push(LengthNodeValueNode[i]); 
        alert(LengthNodeValueNode);

      var builder = require('xmlbuilder');    
     
      var root = builder.create('FileFormat');     
      for(var i = 0; i < filedNames.length; i++)
      {
        //while(nodeNameTest.length!=0){
        var item = root.ele(filedNames[i]);
        var item2 = item.ele('StartPosition',posValue[i]);
        var item3 = item.ele('Length',lenValue[i]);
      }
        // item.att('x');
        // item.att('y');
      //}

     // var item=root.ele('xmlN.map(item=>item.NodeName)');
      var xml = root.end({ pretty: true});
      console.log(xml);

      //const xmll = JSON.parse(xmlN.fieldData);
      //console.log(xmll);
      //const listXml1 = xmlN.map((item, index) => item.fieldData);
      //console.log(listXml1);
     // setVendorData(listXml);
     const listXml = xmlN.map((item, index) => ({
      FieldName:item.NodeName,
      StartPosition:item.startPosNodeValueNode,
      FieldLength:item.LengthNodeValueNode,
      })
      )
      console.log("test check",listXml);
     
    } catch (e) {
      console.log(e)
    }
  };
  
  const onDisplayClientNameList = async () => {
    try {
      const clientNameResponse = await axios.get(`clientName`);
      //console.log(clientNameResponse.data)
      setLoader(false);

      const clientNameN = clientNameResponse.data;
     // console.log(clientNameN);
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
     // console.log(logNameResponse.data)
      setLoader(false);

      const logNameN = logNameResponse.data;
      //console.log(logNameN)
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
      //console.log(P_CLIENTID)
      const channelResponse = await axios.get(`getChannelID`);
        //  console.log(channelResponse.data)
          setLoader(false);

          const channelN = channelResponse.data;
          //console.log(channelN);
     
          const listChannel=channelN.map((item,index)=> <Option value={item.roleID} key={index} label={item.channeltype}>{item.channeltype}</Option>)
          setChannelData(listChannel);
              
     
    } catch (e) {
      console.log(e)
    }
  };


  const onDisplayModeType = async (P_CLIENTID, value) => {
    try {
      const modeTypeResponse = await axios.get(`getModeType/${P_CLIENTID}/${value}`);
     // console.log(modeTypeResponse.data)
      //setLoader(false);
      const modeTypeN = modeTypeResponse.data;
      //console.log(modeTypeN);
      const listmodeType = modeTypeN.map((item, index) => <Option value={item.modeid} key={index} label={item.modename}>{item.modename}</Option>)
      setModeData(listmodeType);
    } catch (e) {
      console.log(e)
    }
  };

  const onDisplayVendor = async (checkedValues) => {
    try {
      const vendorResponse = await axios.get(`getVendorDetailsByType/${checkedValues}`);
     // console.log(vendorResponse.data)
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
     // console.log(channelResponse.data)
      setLoader(false);

      const channelN = channelResponse.data;
      //console.log(channelN);

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
  //console.log(menuData);

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
    setConfigTable(true);
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
       const FieldNames=formatHistoryResponse.map(item=>item.FieldName);
       setClientName(clientNameSet);
       setFormatID(FormatIDSet);
       setFieldNames(FieldNames);
       alert(tblfieldNames);
       const listXml = formatHistoryResponse.map((item, index) => ({
        FieldName:item.NodeName,
        StartPosition:item.startPosNodeValueNode,
        FieldLength:item.LengthNodeValueNode,
        key: index,
        })
        )

        console.log("test check",listXml[1]);
        var xmlTblField=[];
        alert(listXml.length);
        for(var i=1;i<listXml.length;i++)
        {
          xmlTblField.push(listXml[i]);
        }
        console.log(xmlTblField);

        setXmlTable(xmlTblField);
      
      //  if(JSON.stringify(response.data) === 'Save')
      //  {
      //    alert("user added successfully");
      //  }
      //  else{9
      //    alert("already exist");
      //  }
        //props.history.push("/AddUser",response.data)
  
    } catch (e) {
      console.log(e)
    }
  }


  const onFileConfig = async () => {
    try {

      const validateFields = await form.validateFields();    
       const values = form.getFieldsValue();
       //console.log(values)
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
      // console.log(response.data)
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


  const save = async(record)=> {
  
    const values = form.getFieldsValue();
    console.log("values table test"+values.FieldLength);
    console.log("values table test"+values.StartPosition);
    console.log("values table test"+values.FieldName);

    setFieldLength(values.FieldLength);
    setStartPosition(values.StartPosition);
    setFieldName(values.FieldName);
    if( xmltable.map((item, index) =>{item.FieldName})==values.FieldName){
      const listXml = xmltable.map((item, index) => ({
          FieldName:values.FieldName,
          StartPosition:item.startPosNodeValueNode,
          FieldLength:item.LengthNodeValueNode,
           key: index,
           })
         )
    }
   // window.location.reload(false);
    // console.log(xmltable);    
    // const listXml = xmltable.map((item, index) => ({
    //   FieldName:values.FieldName,
    //   StartPosition:item.startPosNodeValueNode,
    //   FieldLength:item.LengthNodeValueNode,
    //   key: index,
    //   })
    //   )

  }

  const tailLayout = {
    wrapperCol: { offset: 10 },
  };
 

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
    render: text => <a>{text}</a>,
  },
  {
    title: 'Start Position',
    dataIndex: 'StartPosition',    
    editable: true,
    render: (text, record) => (
      <Space size="middle">
        {record.StartPosition}
      </Space>
    ),
  },
  {
    title: 'Field Length',
    dataIndex: 'FieldLength',
    key: 'FieldLength',
    editable: true,
    render: (text, record) => (
      <Space size="middle">
        {record.FieldLength}
      </Space>
    ),
  },
  {
    title: 'operation',
    dataIndex: 'operation',
    render: (_, record) => {
      const editable = isEditing(record);
      return editable ? (
        <span>
          <a
            href="#!;"
            onClick={() => save(record)}
            style={{
              marginRight: 8,
            }}
          >
            Save
          </a>
          <Popconfirm title="Sure to cancel?" onConfirm={cancel}>
            <a>Cancel</a>
          </Popconfirm>
        </span>
      ) : (
        <a  onClick={() => edit(record)}>
          Edit
        </a>
      );
    },
  },
];
const mergedColumns = columns.map((col) => {
  if (!col.editable) {
    return col;
  }

  return {
    ...col,
    onCell: (record) => ({
      record,
      inputType: col.dataIndex === 'StartPosition' ? 'text' : 'text',
      dataIndex: col.dataIndex,
      title: col.title,
      editing: isEditing(record),
    }),
  };
});

  function onFieldChange(e) {
    console.log(`selected Field ${e.target.value}`);      
    //setModeID(value);
    //ongetfieldidentification(value);
}
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
            <div className="site-card-wrapper">
              <Row gutter={16}>
                <Col span={14}>
                  <Card title="File Configuration" bordered={false} style={{ width: 900 }} >
                    <Form layout={"vertical"} size={"large"} form={form} component={false}>
                      <Row gutter={[16, 16]} layout="inline">
                        <Col span={5}><b>
                          <Form.Item
                            label="Client Name"
                            name="P_CLIENTID" 
                            rules={[{ required: true, message: 'required' }]}>
                            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeClientName}>
                              {ClientData}
                            </Select>
                          </Form.Item>
                        </b></Col>
                        <Col span={5}><b>
                          <Form.Item
                            label="Log Type"
                            name="P_VENDORTYPE"
                            rules={[{ required: true, message: 'required' }]}>
                            <Select style={{ width: 150 }} onChange={onChangeLog}>
                              {logData}
                            </Select>
                          </Form.Item>
                        </b></Col>
                        {
                          channelloader ? (
                            <Col span={5}><b>
                              <Form.Item
                                label="Channel Type"
                                name="P_CHANNELID"
                                rules={[{ required: true, message: 'required' }]}>
                                <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeChannel}>
                                  {ChannelData}
                                </Select>
                              </Form.Item>
                            </b></Col>
                          ) : ("")}
                        {
                          channelloader ? (
                            <Col span={5}><b>
                              <Form.Item
                                label="Mode Type"
                                name="P_MODEID"
                                rules={[{ required: true, message: 'required' }]}>
                                <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeMode}>
                                  {modeData}
                                </Select>
                              </Form.Item>
                            </b></Col>
                          ) : ("")
                          }
                      </Row>
                      <Row gutter={[16, 16]} layout="inline">
                        <Col span={5}><b>
                          <Form.Item
                            label="File Type"
                            name="P_FILEXML"
                            rules={[{ required: true, message: 'required' }]}>
                            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeFileType}>
                              <Option value={"spreadsheet"}>spreadsheet</Option>
                              <Option value={"plaintext"}>plaintext</Option>
                              <Option value={"plaintextwithSeparator"}>plaintext with Separator</Option>
                            </Select>
                          </Form.Item>
                        </b></Col>
                        <Col span={5}><b>
                          <Form.Item
                            label="File Extention"
                            name="P_FILEEXT"
                            rules={[{ required: true, message: 'required' }]}>
                            {
                              filetype ? (
                                <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeExt}>
                                  <Option value={".xls"}>.xls</Option>
                                  <Option value={".csv"}>.csv</Option>
                                </Select>
                              ) : fileloader ? (
                                <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeExt}>

                                  <Option value={".txt"}>.txt</Option>
                                  <Option value={".RC"}>.RC</Option>

                                </Select>
                              ) : (
                                    <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeExt}>
                                      <Option value={".txt"}>.txt</Option>
                                    </Select>
                                  )
                            }

                          </Form.Item>
                        </b></Col>
                        <Col span={5}><b>

                          <Form.Item
                            label="File Prefix"
                            name="P_FILEPREFIX"
                          //rules={[{ required: true, message: 'Please input your Client name!' }]}
                          >
                            <Input name="P_FILEPREFIX" style={{ width: 150 }} onChange={onFilePre} />
                          </Form.  Item>
                        </b></Col>
                        <Col span={5}><b>
                          {separatorloader ? (
                            <Form.Item
                              label="Separator Type"
                              name="P_SEPARATORTYPE"
                            //rules={[{ required: true, message: 'Please input your Client name!' }]}
                            >
                              <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChange}>
                                <Option value={"|"}>|</Option>
                                <Option value={"||"}>||</Option>
                                <Option value={"@"}>@</Option>
                                <Option value={"%"}>%</Option>
                                <Option value={","}>,</Option>
                                <Option value={":"}>:</Option>
                                <Option value={"!"}>!</Option>
                              </Select>
                            </Form.Item>
                          ) : ("")}
                        </b></Col>

                      </Row>
                      <Row gutter={[16, 16]} layout="inline">
                        <Col span={5}><b>
                          <Form.Item
                            label="Vendor"
                            name="P_VENDORID"
                            rules={[{ required: true, message: 'required' }]}>
                            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeVendor}>
                              {vendorData}
                            </Select>
                          </Form.Item>
                        </b></Col>
                        <Col span={5}><b>
                          <Form.Item
                            label="Cut Off Time:"
                            name="P_CUTOFFTIME"
                            //rules={[{ required: true, message: 'required' }]}
                          >
                            <TimePicker
                              defaultValue={moment('12:08', format)}
                              format={format}
                              name="P_CUTOFFTIME"
                              onChange={onCutOff}
                            />
                        </Form.Item>
                        </b></Col>
                        <Col span={5}><b>
                          <Form.Item
                            label="Start Line No:"
                            name="StartLineNo:"
                          //rules={[{ required: true, message: 'required' }]}
                          >
                            <Input style={{ width: 150 }} />
                          </Form.Item>
                        </b></Col>
                        <Col span={5}><b>
                          <Form.Item
                            label="End Line No:"
                            name="EndLineNo:"
                          //rules={[{ required: true, message: 'required' }]}
                          >
                            <Input style={{ width: 150 }} />
                          </Form.Item>
                        </b></Col>
                      </Row>
                      <Row>
                        <Form.Item>
                          <Button type={"primary"} size={"large"} style={{width:'100px'}} onClick={onFileConfig} >Save</Button>
                          <Button type={"danger"} size={"large"} style={{ margin: '55px' },{width:'100px'}} onClick={onReset} >reset</Button>
                        </Form.Item>
                      </Row>
                      {configLoader?(
                       <Card title={"Configured Format"} bordered={false} style={{ width: 800 }}>

                       <Table columns={columnsConfigFormat} dataSource={ConfigTableData}
                         scroll={{ y: 540 }} bordered />
                     </Card>
                    ):("")}                   

                    </Form>
                  </Card>

                </Col>
                <Col span={10}>
                  {clientInfoCard?(
                  <Card bordered={false} style={{ width: 600 }}   style={{height:"100%"}}>
                    <Form  title="" layout={"horizontal"} size={"large"} form={form}>
                      <div>
                        <b>
                      <Form.Item
                        label="Client Name:"
                        name="clientname"
                      >
                        {cname}
                            </Form.Item>
                            </b>
                      <Row gutter={4} layout="inline">
                        <Col flex={1}><b>
                          <Form.Item
                            label="Format No:"
                            name="Format No"
                          // rules={[{ required: true, message: 'Please input your Country Code !' }]}
                          >
                            {fid}
                        </Form.Item>
                        </b></Col>
                        <Col flex={5}><b>
                          <Form.Item
                            label="Format Type:"
                            name="Format Type"
                          >
                            Current
                          </Form.Item>
                        </b></Col>
                      </Row>
                      <Form.Item
                        label="Format Set to Other Client:"
                        name="Format Set to Other Client:"
                      >
                        <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeClientName}>
                          {ClientData}
                        </Select>
                      </Form.Item>
                    </div>
                    <div   style={{height:"100%"}} >
                    <Form form={form} component={false}>
                    <Table 
                            components={{
                              body: {
                                cell: EditableCell,
                                 
                              },
                            }}                            
                         columns={mergedColumns}  dataSource={xmltable}
                         pagination={false} 
                         rowClassName="editable-row"
                         scroll={{ y: 800 }}
                         bordered                       
                         />   
                         </Form>            
                    </div>
                   
                    </Form>
                  </Card>
                  ):("")}
                </Col>
              </Row>
            </div>           
          </Content>
        </Layout>
      </Layout>
    </Layout >
  );
};
export default FileConfiguration;