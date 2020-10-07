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
} from 'antd';
import Password from 'antd/lib/input/Password';
import { LayoutContext } from 'antd/lib/layout/layout';
import Title from 'antd/lib/typography/Title';
const {Header, Footer ,Sider,Content}=Layout;
const { Option } = Select;

const MatchingRuleConfiguration = props => {
  console.log(props)  
  const [clientid, setClientID] = useState([])
  const [ruletype,setRultType] = useState([])
  const [channeldata,setChannelData] = useState([])
  const [ClientData, setClientData] = useState([])
  const [modeData,setModeData]=useState([])
  const [channelid,setChannelID]=useState([])
  const [modeid,setModeID]=useState([])
  const [columnname,setCOlumnName]=useState([])

//   const [branchdata, setBranchData] = useState([])
   const [loader, setLoader] = useState(true)


useEffect(() => {
//   onDisplayUserRole();
//   onDisplayChannel();
//   onDisplayBranch();
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
const onGetChannelDetails = async (value) => {
    try {
      let selectedclientID=value;
      alert(selectedclientID);
      const channelResponse = await axios.get(`getchanneldetails/${selectedclientID}`);
      //console.log(channelResponse.data)
      setLoader(false);

      const channelN = channelResponse.data;
      //console.log(channelN);

      const listChannel = channelN.map((item, index) => <Option value={item.channelid} key={index} label={item.channelName}>{item.channelName}</Option>)
      setChannelData(listChannel);



    } catch (e) {
      console.log(e)
    }
  };

  const ongetmatchingmodeinfo = async (value) => {
    try {
        ///alert("client id"+ClientData);
        //alert("channel id"+value);
        const modeResponse = await axios.get(`getmatchingmodeinfo/${clientid}/${value}`);
        console.log(modeResponse.data)
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


const ongetMatchingRuleSetForClient= async (value) => {
    try {
        ///alert("client id"+ClientData);
        //alert("channel id"+value);

  const modeResponse = await axios.get(`getMatchingRuleSetForClient/${clientid}/${channelid}/${value}`);
        console.log(modeResponse.data)
        setLoader(false);

        //const modeN = modeResponse.data;
        //console.log(modeN);
        //const branch = JSON.parse(modeN.branchName);
        //console.log(branch);
        //const listMode = modeN.map((item, index) => <Option value={item.ModeID} key={index}>{item.TransactionMode}</Option>);
        //setModeData(listMode);
        
    } catch (e) {
        console.log(e)
    }
};

const addMatchingRuleDetails = async () => {

    try {
        const validateFields = await form.validateFields();
        const values = form.getFieldsValue();
        console.log(values)
        //clientid}/{columnname}/{channelid}/{modeid}/{ruletype}")
        ///addfieldconfig/{}/{P_VENDORID}/{P_FORMATID}/{P_TERMINALCODE}/{}/{}/{}/{}/{}/{}/{}/{}/{}/{}/                                                                                                                                                                                                                                             {}/{}/{}/{}/{}/{}/{}/{}/{}/{}/{}/                                                                                                                                                                                                               {}/{}/{}/{}/{}/{}/{}
       const response = await axios.get(`getaddmatchingruleconfig/${clientid}/${columnname}/${channelid}/${modeid}/${ruletype}`);
       console.log(response.data);
       const responseAddRule=response.data;
       const rdata = responseAddRule.map((item, index) => item.Status );
        console.log(rdata);
        const abc=JSON.stringify(rdata); 
         console.log(abc);
       if (JSON.stringify(rdata) === '["[record insert]"]') {
            alert("inserted successfully");
        }
        

       window.location.reload(false);

        //props.history.push("/AddUser",response.data)
    } catch (e) {
        console.log(e);
    }
}


 
  const menuData = props.location.state;
  console.log(menuData);

  function onChange(e) {
    console.log(`checked = ${e.target.checked}`);
  }

  function onChangeColumnName(checkedValues) {
    console.log('checked = ', checkedValues);
    setCOlumnName(checkedValues);
  }

  function onChangeRuleType(value) {
    console.log(`selected ${value}`);
    setRultType(value);
    ongetMatchingRuleSetForClient(value);
  }
  function onChangeTxnMode(value) {
    console.log(`selected ${value}`);
    setModeID(value);
  }
  function onChangeChanneltName(value) {
    console.log(`selected ${value}`);
    setChannelID(value);
    ongetmatchingmodeinfo(value)
  }
  
  function handleChange(value) {
    console.log(`selected ${value}`);
  }

  const [form] = Form.useForm()

//   const onAddUser= async()=> {

//     try{
//     const validateFields = await form.validateFields();    
//     const values = form.getFieldsValue();
//     console.log(values)
//     const response = await axios.get(`AddUser/${values.clientId}/${values.FirstName}/${values.LastName}/${values.UserId}/${values.EmailID}/${values.Channel}/${values.RoleType}/${values.Password}/${values.ConfirmPassword}/${values.BranchName}/${values.ContactNo}`);
//     console.log(response.data)
      
//     if(JSON.stringify(response.data) === 'Save')
//     {
//       alert("user added successfully");
//     }
//     else{
//       alert("already exist");
//     }
//      //props.history.push("/AddUser",response.data)
//     }catch(e) {
//         console.log(e)
//     }
// }

  
  function onChange(checkedValues) {
    console.log('checked = ', checkedValues);
  }
  function onChangeClientName(value) {
    console.log(`selected ${value}`);
    setClientID(value)
    onGetChannelDetails(value);
   
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
    <Card title="Matching Rule Configuration" bordered={false} style={{ width: 800 }} >
      
    <Form  initialValues={{ remember: true }} layout={"vertical"}  form={form} size={"large"}>
    <Row>
      <Col span={24}>
      <Form.Item label="Client Name" name="clientId" >
         <Select defaultValue="--select--" style={{ width: 700 }} onChange={onChangeClientName}>
                              {ClientData}
                            </Select>                  
                  </Form.Item>
       
       </Col>
    </Row>
    <Row>
       <Col span={24}>
         <Form.Item label="Channel Type" name="ChannelType" >
          <Select defaultValue="--select--" style={{ width: 700 }} onChange={onChangeChanneltName}>
                              {channeldata}
           </Select>                  
         </Form.Item>
       
       </Col>
    </Row>
    <Row gutter={[16, 16]}>
       <Col span={12} >
       <Form.Item label="Rule Type" name="RuleType" >
          <Select  style={{ width: 300 }} onChange={onChangeRuleType}>
            <Option value="0">--Select--</Option>
            <Option value="1">Matching Rule</Option>
            <Option value="2">Settlement Rule</Option>
           </Select>                  
         </Form.Item>
       </Col>
       <Col span={12} >
       <Form.Item label="Txn Mode" name="Txnmode" >         
           <Select
                        mode="multiple"
                        style={{ width: 300 }}
                        placeholder="select channels"
                        onChange={onChangeTxnMode}
                        optionLabelProp="label"
                      >
                        {modeData}
                </Select>                 
         </Form.Item>
       </Col>       
     </Row>
     <Checkbox.Group style={{ width: '100%' }} onChange={onChangeColumnName}>
    <Row>
      <Col span={8}>
        <Checkbox value="ReferenceNumber">Reference No</Checkbox>
      </Col>
      <Col span={8}>
        <Checkbox value="CardNumber">Card No</Checkbox>
      </Col>
      <Col span={8}>
        <Checkbox value="TerminalID">Terminal ID</Checkbox>
      </Col>
      <Col span={8}>
        <Checkbox value="TxnDate">Txn Date</Checkbox>
      </Col>
      <Col span={8}>
        <Checkbox value="TxnAmount">Txn Amount</Checkbox>
      </Col>
    </Row>
  </Checkbox.Group>      
  
    <Row>
              <Form.Item>
                   <Button onClick={addMatchingRuleDetails} >Submit</Button>             
                   <Button  style={{ margin: '0 8px' }} onClick={props.history.goBack} >Back</Button>           
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
export default MatchingRuleConfiguration;