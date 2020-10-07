import React, { useState,useEffect } from 'react';
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
  DatePicker,
 
} from 'antd';
import Password from 'antd/lib/input/Password';
import { LayoutContext } from 'antd/lib/layout/layout';
import Title from 'antd/lib/typography/Title';
const {Header, Footer ,Sider,Content}=Layout;
const { Option } = Select;
const { Search } = Input;
const ForceSettlementRuleConfiguration = props => {
  console.log(props)  
//   const [roledata,setRoleData] = useState([])
  const [channeldata,setChannelData] = useState([])
  const [ClientData, setClientData] = useState([])
//   const [branchdata, setBranchData] = useState([])
  const [modeData,setModeData]=useState([])
  const [channelid,setChannelID]=useState([])
  const [modeid,setModeID]=useState([])
  const [clientid, setClientId] = useState([])
  const [statusData,setStatusData]=useState([])
  const [ejstatus,setEJStatus]=useState([])
  const [swstatus,setSWStatus]=useState([])
  const [nwstatus,setNWStatus]=useState([])
  const [glstatus,setGLStatus]=useState([])
  const [recontype,setReconTypeID]=useState([])

  const [loader, setLoader] = useState(true)
  const [recontypeloader,setReconType]=useState(false)
  const [modeStatus,setModeStatus]=useState(false)
  const [onusStatus,setOnusStatus]=useState(false);
  const [atmStatus,setAtmStatus]=useState(false);
  const [AcqStatus,setAcqStatus]=useState(false);
  const [IssStatus,setIssStatus]=useState(false);

   const dateFormat = 'YYYY/MM/DD';
   const monthFormat = 'MM/YYYY';

useEffect(() => {
//   onDisplayUserRole();
//   onDisplayChannel();
//   onDisplayBranch();
ongetStatusMaster();
  onDisplayClientNameList();
}, [])

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

const ongetclientreportdetails = async (value) => {
  try {
    const clientReportResponse = await axios.get(`getclientreportdetails/${value}`);
    console.log(clientReportResponse.data)
    setLoader(false);

    /*const clientNameN = clientNameResponse.data;
    console.log(clientNameN);
    const clientNameList = clientNameN.map((item, index) =>
      <Option value={item.id} key={index}>{item.clientNameList}
      </Option>
    )
    setClientData(clientNameList);*/

  } catch (e) {
    console.log(e)
  }
};

const onGetChannelDetails = async (value) => {
  try {
    let selectedclientID=value;
    alert(selectedclientID);
    const channelResponse = await axios.get(`getchanneltypeall/${selectedclientID}`);
    console.log(channelResponse.data)
    setLoader(false);

    const channelN = channelResponse.data;
    //console.log(channelN);

    const listChannel = channelN.map((item, index) => <Option value={item.channelid} key={index} label={item.channelname}>{item.channelname}</Option>)
    setChannelData(listChannel);



  } catch (e) {
    console.log(e)
  }
};
 //@GetMapping("/getModeType/{clientid}/{channelid}")

 const ongetModeType = async (value) => {
  try {
      ///alert("client id"+ClientData);
      //alert("channel id"+value);
      const modeResponse = await axios.get(`getmatchingmodeinfo/${clientid}/${value}`);
      console.log(modeResponse.data)
      setModeStatus(true);
      setLoader(false);

      const modeN = modeResponse.data;
      //console.log(modeN);
      //const branch = JSON.parse(modeN.branchName);
      //console.log(branch);
      const listMode = modeN.map((item, index) => <Option value={item.ModeID} key={index}>{item.TransactionMode}</Option>);
      setModeData(listMode);
      
  } catch (e) {
      console.log(e)
  }
};
//@GetMapping("getStatusMaster")

const ongetStatusMaster = async () => {
  try {
      ///alert("client id"+ClientData);
      //alert("channel id"+value);
      const statusResponse = await axios.get(`getStatusMaster`);
      console.log(statusResponse.data)
      setLoader(false);

      const statusTypeN = statusResponse.data;
      console.log(statusTypeN);
      //const branch = JSON.parse(modeN.branchName);
      //console.log(branch);
      const listStatus = statusTypeN.map((item, index) => <Option value={item.statusid} key={index}>{item.statusname}</Option>);
      setStatusData(listStatus);
      
  } catch (e) {
      console.log(e)
  }
};


  const menuData = props.location.state;
  console.log(menuData);

  function onChange(e) {
    console.log(`checked = ${e.target.checked}`);
  }
  function onChangeRuleType(value) {
    console.log(`selected ${value}`);
  }
  function onChangeModeName(value) {
    console.log(`selected ${value}`); 
    setModeID(value);
    if(value == 1 || value == 3){
      setReconType(true);
    }
    else{
      setReconType(false);
    }

    if(value == 1){
      setOnusStatus(true);
      setAcqStatus(false);
      setIssStatus(false);

    }

    if(value == 2){
      setAcqStatus(true);
      setOnusStatus(false);
      setIssStatus(false);

    }
    
    if(value == 3){
      setIssStatus(true);
      setAcqStatus(false);
      setOnusStatus(false);


    }
  }
  
  
  


  function onChangeReconType(value) {
    console.log(`selected ${value}`);
    setReconTypeID(value)
  }

  function onChangeChanneltName(value) {
    console.log(`selected ${value}`);
    setChannelID(value);
    ongetModeType(value);
  }
  
  function onChangeATM(value) {
    console.log(`selected ${value}`);
    
    setAtmStatus(true)
  }

  function onChangeGLStatus(value) {
    console.log(`selected ${value}`);
    setGLStatus(value);
  }
  
  function onChangeSWStatus(value) {
    console.log(`selected ${value}`);
    setSWStatus(value);
  }
  
  function onChangeEJStatus(value) {
    console.log(`selected ${value}`);
    setEJStatus(value);
  }

  function onChangeNWStatus(value) {
    console.log(`selected ${value}`);
    setNWStatus(value);
  }



  const [form] = Form.useForm()
//@GetMapping("forcesettlementtxns/{clientid}/{channelid}/{modeid}/{glstatus}/{ejstatus}/{nwstatus}/{swstatus}/{fromdatetxn}/{todatetxn}/{recontype}/{settlementtype}/{branchid}")
  const onforcesettlementtxns= async()=> {

    try{
    const validateFields = await form.validateFields();    
    const values = form.getFieldsValue();
    console.log(values)
    const response = await axios.get(`forcesettlementtxns/${clientid}/${channelid}/${modeid}/${glstatus}/${ejstatus}/${nwstatus}/${swstatus}/${values.fromdatetxn}/${values.todatetxn}/${recontype}/${values.SettlementType}`);
    console.log(response.data)
      
    // if(JSON.stringify(response.data) === 'Save')
    // {
    //   alert("user added successfully");
    // }
    // else{
    //   alert("already exist");
    // }
     //props.history.push("/AddUser",response.data)
    }catch(e) {
        console.log(e)
    }
}

  
  function onChange(checkedValues) {
    console.log('checked = ', checkedValues);
  }
  function onChangeClientName(value) {
    console.log(`selected ${value}`);
    setClientId(value);
    ongetclientreportdetails(value);
    onGetChannelDetails(value);
   
  }
  function onChangeSettlementTypeOne(value) {
    console.log(`selected ${value}`);
   
  }
  function onChangeSettlementType(value) {
    console.log(`selected ${value}`);
   
  }
  
  function range(start, end) {
    const result = [];
    for (let i = start; i < end; i++) {
      result.push(i);
    }
    return result;
  }
  function disabledDateTime() {
    return {
      disabledHours: () => range(0, 24).splice(4, 20),
      disabledMinutes: () => range(30, 60),
      disabledSeconds: () => [55, 56],
    };
  }

  return (
    
      <Layout>
         <Header style={{padding:"20px"}}>
            <Avatar shape ="square" style ={{float :"right"}} size="default" src="./max.png"/>
            <Title
             level={3} style={{color:"white"}}>Trace</Title>
            </Header>
        <Layout>
             <MenuSideBar menuData={menuData}/>
      <Layout style ={{height:"100vh",backgroundColor:"white"}}>
      <Content>
    <Card title="Force Settlement Rule Configuration" bordered={false} style={{ width: 800 }} >
      
    <Form  initialValues={{ remember: true }} layout={"vertical"}  form={form} size={"large"}>
    <Row gutter={[16, 16]}>
        <Col span={6} >
            <Form.Item label="Client Type" name="ClientType" >
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeClientName}>
                                {ClientData}
            </Select>                  
            </Form.Item>
        </Col>
        <Col span={6} >
            <Form.Item label="Channel Type" name="ChannelType" >
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeChanneltName}>
                                {channeldata}
            </Select>                  
            </Form.Item>
        </Col>
        <Col span={6} >

            <Form.Item label="Mode Type" name="Mode Type" >
           
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeModeName}>
                                {modeData}
            </Select>                  
            </Form.Item>
        </Col>
        <Col span={6} >
            <Form.Item label="Recon Type" name="ReconType" >
           
                 {
                    recontypeloader ? (
                  <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeReconType}>
                  
                    <Option value="0">--ALL--</Option>
                    <Option value="1">1-Way</Option>
                    <Option value="2">2-Way</Option>
                    </Select>
                   ):
                   (
                    <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeReconType}>
                  <Option value="0">--ALL--</Option>
                  <Option value="1">1-Way</Option>
                  <Option value="2">2-Way</Option>
                  <Option value="3">3-Way</Option>
                    </Select>
                   )
                 }
                  
                         
            </Form.Item>
        </Col>
        
    </Row>
    <Row gutter={[16, 16]}>
        <Col span={6} >
            <Form.Item label="Settlement Type" name="SettlementType" >
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeSettlementTypeOne}>
                  <Option value="0">--Select--</Option>
                  <Option value="1">Successful</Option>
                  <Option value="2">Reversal</Option>
                  <Option value="3">Partial Reversal</Option>
                  <Option value="4">Unsuccessful</Option>
            </Select>                  
            </Form.Item>
        </Col>
        <Col span={6} >
            <Form.Item label="From Date" name="fromdatetxn" >
            {/* <DatePicker picker={"date"}  format={dateFormat}disabledTime={disabledDateTime}  style={{width:150 }} />  */}
            <input type="date"/>
               
            </Form.Item>
        </Col>
        <Col span={6} >
            <Form.Item label="To Date" name="todatetxn" >
            {/* <DatePicker picker={"date"}  format={dateFormat} disabledTime={disabledDateTime} style={{width:150 }} /> */}
            <input type="date"/>
            </Form.Item>
        </Col>
        <Col span={6} >
            <Form.Item label="Search" name="Search" >
            <Search
                placeholder="input search text"
                onSearch={value => console.log(value)}
                style={{ width: 200 }}
            />
            </Form.Item>
        </Col>
        
    </Row>
    {onusStatus ? (
       <Checkbox.Group style={{ width: '100%' }} onChange={onChangeATM}>
      <Row>
        <Col span={8}>
          <Checkbox value="GLStatus">GL Status</Checkbox>
          <br/>
          {atmStatus ? (
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeGLStatus}>
            {statusData}
          </Select>     
          ):("") 
          }
        </Col>
        <Col span={8}>
          <Checkbox value="SWStatus" >SW Status</Checkbox>
          <br/>
          {atmStatus ? (
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeSWStatus}>
            {statusData}
          </Select>     
          ):("") 
          }
           <br/>
        </Col>
        <Col span={8}>
          <Checkbox value="EJStatus">EJ Status</Checkbox>
          <br/>
          {atmStatus ? (
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeEJStatus}>
            {statusData}
          </Select>     
          ):("") 
          }
           <br/>
        </Col>
        
      </Row>
     </Checkbox.Group>  
    ):("") 
    }
      {AcqStatus ? (
      <Checkbox.Group style={{ width: '100%' }} onChange={onChangeATM}>

      <Row>
        <Col span={6}>
          <Checkbox value="GLStatus">GL Status</Checkbox>
          <br/>
          {atmStatus ? (
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeGLStatus}>
            {statusData}
          </Select>     
          ):("") 
          }
        </Col>
        <Col span={6}>
          <Checkbox value="SWStatus">SW Status</Checkbox>
          <br/>
          {atmStatus ? (
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeSWStatus}>
            {statusData}
          </Select>     
          ):("") 
          }
           <br/>
        </Col>
        <Col span={6}>
          <Checkbox value="NWStatus">NW Status</Checkbox>
          <br/>
          {atmStatus ? (
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeNWStatus}>
            {statusData}
          </Select>     
          ):("") 
          }
           <br/>
        </Col>
        <Col span={6}>
          <Checkbox value="EJStatus">EJ Status</Checkbox>
          <br/>
          {atmStatus ? (
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeEJStatus}>
            {statusData}
          </Select>     
          ):("") 
          }
           <br/>
        </Col>
        
      </Row>
    </Checkbox.Group>  
    ):("") 
    }        

{IssStatus ? (
      <Checkbox.Group style={{ width: '100%' }} onChange={onChangeATM}>

      <Row>
        <Col span={8}>
          <Checkbox value="GLStatus">GL Status</Checkbox>
          <br/>
          {atmStatus ? (
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeGLStatus}>
            {statusData}
          </Select>     
          ):("") 
          }
        </Col>
        <Col span={8}>
          <Checkbox value="SWStatus">SW Status</Checkbox>
          <br/>
          {atmStatus ? (
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeSWStatus}>
            {statusData}
          </Select>     
          ):("") 
          }
           <br/>
        </Col>
        <Col span={8}>
          <Checkbox value="NWStatus">NW Status</Checkbox>
          <br/>
          {atmStatus ? (
            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeNWStatus}>
            {statusData}
          </Select>     
          ):("") 
          }
           <br/>
        </Col>
        
      </Row>
    </Checkbox.Group>  
    ):("") 
    }   

    <Row>
              <Form.Item>
                
<br/>
                   <Button onClick={onforcesettlementtxns}>Show</Button>             
                   <Button  style={{ margin: '0 8px' }} ><Checkbox onChange={onChange}>Select All</Checkbox></Button>           
              </Form.Item>         
    </Row>
      </Form>
      </Card>
      </Content>
      </Layout>
      </Layout>
      </Layout>
  );
};
export default ForceSettlementRuleConfiguration;