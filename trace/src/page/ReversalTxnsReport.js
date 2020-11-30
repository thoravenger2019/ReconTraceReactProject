import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import axios, { axiosGet } from '../utils/axios';
import MenuSideBar from './menuSideBar';
import moment from 'moment';

import {
  Form,
  Button,
  Select,
  Card,
  Row,
  Col,
  Checkbox,
  Layout,
  Avatar,
  Input,
  DatePicker,
  Table,
} from 'antd';
import Password from 'antd/lib/input/Password';
import Title from 'antd/lib/typography/Title';
const { Header, Footer, Sider, Content } = Layout;
const FormItem = Form.Item;
const { Option } = Select;

const ReversalTxnsReport = props => {
  console.log(props)

  const [data, setData] = useState([])
  const [loader, setLoader] = useState(true)
  const [clientid, setClientId]=useState([])
  const [clientData,setClientData]=useState([])
  const [channelData,setChannelData]=useState([])
  const [modeData,setModeData]=useState([])
  const [dispenseSummaryReoprttbldata,setDispenseSummaryReort]=useState([])
  const [selectedFileData, setStateFile] = useState(undefined)
  const [setTerm,setTerminal]=useState(false)
  const [setTxnType,setTxn]=useState(false);
  useEffect(() => {
    //onDisplayImplortFile();
    onDisplayClientNameList();
  }, [])

  function onChangeClientName(value) {
    console.log(`selected ${value}`);  
    setClientId(value);
    onDisplayChannel(value); 
  }

  function onChangeMode(value) {
    console.log(`selected ${value}`);  
    if(JSON.stringify(value)=="1")  
    {
      setTerminal(true);
      
    }
    //setClientId(value);
   // onDisplayChannel(value); 
  }
  function onChangeChannel(value) {
    console.log(`selected ${value}`);  
    if(JSON.stringify(value)=="1"){
      setTxn(true);
      
    }
    //setClientId(value);
    ongetModeType(value); 
  }

  const onDisplayClientNameList = async () => {
    try {
        const clientNameResponse = await axios.get(`clientName`);
        console.log(clientNameResponse.data)
        setLoader(false);

        const clientNameN = clientNameResponse.data;
        console.log(clientNameN);
       
        //const role = JSON.parse(roleN.roleNames);
        //console.log(role);
      //  const firstClient=clientNameN[0];
        const clientNameList =clientNameN.map((item, index) =>
        <Option value={item.id} key={index}>{item.clientNameList}
        </Option>
        )
        setClientData(clientNameList);

    } catch (e) {
        console.log(e)
    }
};

/*
@GetMapping("/getchanneltyperun/{clientid}")
@GetMapping("/getModeTypeRun/{clientid}/{channelid}")
*/

const onDisplayChannel = async (value) => {
  try {
    const channelResponse = await axios.get(`getchanneltyperun/${value}`);
    console.log(channelResponse.data)
    setLoader(false);

    const channelN = channelResponse.data;
    console.log(channelN);

    const listChannel = channelN.map((item, index) => <Option value={item.channelID} key={index} label={item.channelName}>{item.channelName}</Option>)
    setChannelData(listChannel);

  } catch (e) {
    console.log(e)
  }
};

const ongetModeType = async (value) => {
  try {
      ///alert("client id"+ClientData);
      //alert("channel id"+value);
      const modeResponse = await axios.get(`getModeTypeRun/${clientid}/${value}`);
      console.log(modeResponse.data)
      // setModeStatus(true);
      setLoader(false);

      const modeN = modeResponse.data;
      //console.log(modeN);
      //const branch = JSON.parse(modeN.branchName);
      //console.log(branch);
      const listMode = modeN.map((item, index) => <Option value={item.modeid} key={index}>{item.modename}</Option>);
      setModeData(listMode);
      
  } catch (e) {
      console.log(e)
  }
};

  const onDisplayImplortFile = async () => {
    try {
      
      const importFileResponse = await axios.get(`getUploadFiletype`);
      console.log(importFileResponse.data)
      setLoader(false);

      const fileN = importFileResponse.data;
      console.log(fileN);

      const listFile = fileN.map((item, index) => <Option value={item.id} key={index}>{item.fileType}</Option>)
      setData(listFile);

      //console.log(dataAll);

    } catch (e) {
      console.log(e)
    }
  };

  const menuData = props.location.state;
  console.log(menuData);

  function onChange(value) {
    console.log(`selected ${value}`);
  }
  function handleChange(value) {
    console.log(`selected ${value}`);
  }

  const [form] = Form.useForm()
  

  const onFileUpload = async () => {
    try {
      /*const validateFields = await form.validateFields()     
     const values = form.getFieldsValue();
     console.log(values)
    const response = await axios.get(`login1/${values.username}/${values.password}`);
     console.log(response.data)
     setMenuData(response.data)
     props.history.push("/dashboard",response.data)
      value={myStateValue || ''}
*/
     // const validateFields = await form.validateFields();    
     // const values = form.getFieldsValue();
      let currentFile=selectedFileData[0];
      const formData = new FormData();            
      formData.append('file',currentFile);
      console.log(currentFile);
      console.log(formData);
      const response = await axios.post(`importFile`,formData);
    } catch (e) {
      console.log(e)
    }
  }

  const onShowReversaltxnreport=async()=>{
    const validateFields=await form.validateFields();
    const values = form.getFieldsValue();
    console.log(values); 
    const reversalReport = await axios.get(`getreversaltxnreport/${values.clientID}/${values.channelID}/${values.modeID}/${values.terminalID}/${values.fromDateTxns}/${values.toDateTxns}/${values.txnType}`);
    console.log(reversalReport.data)
    const reversalReporttbl=reversalReport.data;
    const dataAll = reversalReporttbl.map((item, index) => ({
      TxnsDate: item.TXNDATE,
      TerminalID: item.TERMINALID,
      ATMDiff: item.ATMDIFF,
      EJTotal: item.EJTOTAL,
      GLAcquirer:item.GLACQUIRER,
     // GLISSUER:item.GLISSUER,
      GLOnus:item.GLONUS,
      GLTotal:item.GLTOTAL,
      NFSAcquirer:item.NFSACQUIRER,
      NFSAcquirerDiff:item.NFSACQUIRERDIFF,
     // GLONUS:item.NFSISSUER,
      //GLONUS:item.NFSISSUERDIFF,
      //GLONUS:item.SWISSUER,
      SwitchTotal:item.SWITCHTOTAL,
      UnSettledAmount:item.UNSETTLEDAMOUNT,
      key: index,
      size: '15px'
    })
    )
    setDispenseSummaryReort(dataAll);
 
  }
  const { RangePicker } = DatePicker;

  const dateFormat = 'DD/MM/YYYY';
  const monthFormat = 'MM/YYYY';

  const onChangeHandler = event => {     
    setStateFile(event.target.files) 
}
  const [componentSize, setComponentSize] = useState('small');

  const onFormLayoutChange = ({ size }) => {
    setComponentSize(size);
  };
  const tailLayout = {
    wrapperCol: { offset: 10 },
  };
  const FormItem = Form.Item;

  function onChange(checkedValues) {
    console.log('checked = ', checkedValues);
  }

  const columns = [
    {
      title: 'Txns Date',
      dataIndex: 'TxnsDate',
      key: 'TxnsDate',
      render: text => <a>{text}</a>,
    },
    {
      title: 'Terminal ID',
      dataIndex: 'TerminalID',
      key: 'TerminalID',
    },
    {
      title: 'GL Onus',
      dataIndex: 'GLOnus',
      key: 'GLOnus',
    },
    {
      title: 'GL Acquirer',
      dataIndex: 'GLAcquirer',
      key: 'GLAcquirer',
    },
    {
      title: 'GL Total',
      dataIndex: 'GLTotal',
      key: 'GLTotal',
    },
    {
      title: 'Switch Total',
      dataIndex: 'SwitchTotal',
      key: 'SwitchTotal',
    },    
    {
      title: 'EJ Total',
      dataIndex: 'EJTotal',
      key: 'EJTotal',
    },
    {
      title: 'NFS  Acquirer',
      dataIndex: 'NFSAcquirer',
      key: 'NFSAcquirer',
    },
    {
      title: 'NFS Acquirer Diff',
      dataIndex: 'NFSAcquirerDiff',
      key: 'NFSAcquirerDiff',
    },
    {
      title: 'ATM Diff',
      dataIndex: 'ATMDiff',
      key: 'ATMDiff',
    },
    {
      title: 'UnSettled Amount',
      dataIndex: 'UnSettledAmount',
      key: 'UnSettledAmount',
    }
  ];
  


  return (
    <Layout>
      <Header style={{ padding: "20px" }}>
        <Avatar shape="square" style={{ float: "right" }} size="default" src="./max.png" />
        <Title level={3} style={{ color: "white" }}>Trace</Title>
      </Header>
      <Layout>
        <MenuSideBar menuData={menuData} />
        <Layout style={{ height: "100vh", backgroundColor: "white" }}>
          <Content>
            <Card title="Reversal Txns Report" bordered={false} style={{ width: 1500 }}>
              <Form initialValues={{ remember: true }} layout={"vertical"} size={"large"} form={form}>
                <Row  gutter={8} >
                    <Col span={4}>
                    <Form.Item label="Client Name" name="clientID" >
                        <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeClientName}>
                              {clientData}
                           </Select>                  
                    </Form.Item>

                    </Col>
                    <Col span={4}>
                      
                      <Form.Item label="Channel Type" name="channelID" >
                        <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeChannel}>
                              {channelData}
                        </Select>                  
                    </Form.Item>

                    </Col>
                    <Col span={4}>
                      <Form.Item label="Mode Type" name="modeID" >
                          <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeMode}>
                                {modeData}
                          </Select>                  
                      </Form.Item>
                    </Col>
                    <Col span={4}>
                    {setTxnType?(
                      <Form.Item label="Txn Type" name="txnType" >
                       
                           <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeMode}>
                           <Option value="0">--Select--</Option>
                           <Option value="Withdrawal">Withdrawal</Option>
                           <Option value="Deposit">Deposit</Option>
                      </Select> 
                      </Form.Item> 
                        ):("")}                                         
                    </Col>
                    <Col span={4}>
                    {setTerm?(
                      <Form.Item label="Terminal" name="terminalID" >
                        <Select defaultValue="ALL" style={{ width: 200 }} onChange={onChangeMode}>
                          <Option value="0">ALL</Option>
                          </Select> 
                          </Form.Item>):("")}
                      </Col>
                </Row>
                <Row  gutter={4} style={{ height: '50%' }}  >
                <Col span={6}>
                <Form.Item label=" " name="fromDateTxns" style={{width: 320}}>    
                 
                  {/* <DatePicker  format={dateFormat} style={{width:320 }} /> */}
                  <Input type={"date"}></Input>
                  
                </Form.Item>
                </Col>
                <Col span={6}>
                <Form.Item label=" " name="toDateTxns" style={{width: 320}}> 
                 
                  <Input type={"date"}></Input>
                  {/* <DatePicker format={dateFormat} style={{width: 320}}  /> */}
                  
                  </Form.Item>
                  </Col>
                </Row>               
                <Row  gutter={8}>  
                  <Form.Item label=" " name="">             
                     <Button type={"primary"} size={"large"} style={{width:'100px'}} onClick={onShowReversaltxnreport}>Show</Button>       
                  </Form.Item>           
                </Row>
              </Form>
              {/* <Table columns={columns} dataSource={dispenseSummaryReoprttbldata}/> */}
            </Card>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};
export default ReversalTxnsReport;