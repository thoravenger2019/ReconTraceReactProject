import React, { useState,useEffect } from 'react';
import 'antd/dist/antd.css';
import axios from '../utils/axios';
import MenuSideBar from './menuSideBar';
import {
  Form,
  Input,
  Button,
  Select,
  Card,
  Row,
  Col,
  Layout,
  Avatar,
} from 'antd';
import Password from 'antd/lib/input/Password';
import Title from 'antd/lib/typography/Title';
const {Header, Content}=Layout;

const { Option } = Select;

const AddUser = props => {
  console.log(props)  
  const [roledata,setRoleData] = useState([])
  const [channeldata,setChannelData] = useState([])
  const [ClientData, setClientData] = useState([])
  const [branchdata, setBranchData] = useState([])
  const [loader, setLoader] = useState(true)
  const [clientId,setClientID]=useState([])

useEffect(() => {
  
  onDisplayChannel();
 
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


  const onDisplayUserRole = async(value)=> {
    try{
          const roleResponse = await axios.get(`GetRoleDetails/${value}`);
          console.log(roleResponse.data)
          setLoader(false);

          const roleN = roleResponse.data;
          console.log(roleN);

          const role = JSON.parse(roleN.roleNames);
          console.log(role);

           const listFile=role.map((item,index)=> <Option value={item.roleID} key={index}>{item.roleNAme}</Option>)
           setRoleData(listFile);
                  
       }catch(e) {
         console.log(e)
       }
   };
  
   
   const onDisplayBranch = async(value)=> {
    try{
      //alert(clientId);
      const branchResponse = await axios.post(`getbranchname/${value}`);
      console.log(branchResponse.data)
          setLoader(false);

          const branchN = branchResponse.data;
          console.log(branchN);
          // const branch = JSON.parse(branchN.branchName);
          // console.log(branch);
          const listBranch=branchN.map((item,index) => <Option value={item.branchcode} key={index}>{item.branchname}</Option>)
          setBranchData(listBranch);
      
       }catch(e) {
         console.log(e)
       }
   };

   const onDisplayChannel = async()=> {
    try{
          const channelResponse = await axios.get(`getChannelID`);
          console.log(channelResponse.data)
          setLoader(false);

          const channelN = channelResponse.data;
          console.log(channelN);
     
          const listChannel=channelN.map((item,index)=> <Option value={item.roleID} key={index} label={item.vendorType}>{item.channeltype}</Option>)
          setChannelData(listChannel);
              
       }catch(e) {
         console.log(e)
       }
   };
   
  const menuData = props.location.state;
  console.log(menuData);

  function onChangeClient(value) {
    console.log(`selected ${value}`);
    setClientID(value);
    onDisplayBranch(value);
    onDisplayUserRole(value);
  }
  function onChange(value) {
    console.log(`selected ${value}`);
  }
  // function handleChange(value) {
  //   console.log(`selected ${value}`);
  // }

  const [form] = Form.useForm()

  const onAddUser= async()=> {

    try{
    //const validateFields = await form.validateFields();    
    const values = form.getFieldsValue();
    console.log(values)
    const response = await axios.get(`AddUser/${values.clientId}/${values.FirstName}/${values.LastName}/${values.UserId}/${values.EmailID}/${values.RoleType}/${values.Password}/${values.ConfirmPassword}/${values.BranchID}/${values.ContactNo}`);
    console.log(response.data)
      
    if(JSON.stringify(response.data) === '[Save]')
    {
      alert("user added successfully");
    }
    else{
      alert("already exist");
    }
     //props.history.push("/AddUser",response.data)
    }catch(e) {
        console.log(e)
    }
}

  
  // function onChange(checkedValues) {
  //   console.log('checked = ', checkedValues);
  // }
  // function onChangeClientName(value) {
  //   console.log(`selected ${value}`);
   
  // }
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
    <Card title="User Resgistraion" bordered={false} style={{ width: 800 }} >
      
    <Form  initialValues={{ remember: true }} layout={"vertical"}  form={form}>
    <Form.Item label="Client Name" name="clientId" >
    <Select defaultValue="--select--" style={{ width: 600 }} size={"large"} onChange={onChangeClient}>
                              {ClientData}
                            </Select>                  
                  </Form.Item>
                  
         <Row gutter={8} layout="inline">
            <Col xs={2} sm={16} md={12} lg={8} xl={10}><b>
                <Form.Item 
                    label="First Name"
                    name="FirstName"
                    rules={[{ required: true, message: 'Please input your First name!' }]}>
                   <Input  size="large"/>
                    </Form.Item>    
                    
            </b></Col>
            <Col xs={2} sm={16} md={12} lg={8} xl={10}><b>
                <Form.Item 
                    label="Last Name"
                    name="LastName"
                    rules={[{ required: true, message: 'Please input your last name!' }]}><Input  size="large"/></Form.Item>    
            </b></Col>  
        </Row>            
        <Row gutter={8}>
            <Col xs={2} sm={16} md={12} lg={8} xl={10}><b>
                <Form.Item 
                    label="User ID"
                    name="UserId"
                    rules={[{ required: true, message: 'Please input your User ID!' }]}>
                    <Input  size="large"/> 
                    </Form.Item>          
            </b></Col>
            <Col xs={2} sm={16} md={12} lg={8} xl={10}><b>
                <Form.Item 
                    label="Email ID"
                    name="EmailID"
                    rules={[{ required: false, message: 'Please input your Email!' }]}><Input  size="large"/></Form.Item>    
            </b></Col>  
        </Row>
        <Row gutter={8}>
        <Col xs={2} sm={16} md={12} lg={8} xl={10}><b>
                <Form.Item 
                    label="Role Type"
                    name="RoleType"
                    rules={[{ required: true, message: 'Please input your Role Type!' }]}>
                    <Select onChange={onChange}  size="large" >
                    {roledata}
                  </Select>
                    </Form.Item>    
            </b></Col>  
        <Col xs={2} sm={16} md={12} lg={8} xl={10}><b>
                <Form.Item 
                    label="Branch Name"
                    name="BranchID"
                    rules={[{ required: true, message: 'Please input your Branch Name!' }]}>
                    <Select onChange={onChange}  size="large">
                        {branchdata}
                    </Select>                    
                </Form.Item>    
            </b></Col>           
        </Row>
        <Row gutter={8}>
            <Col xs={2} sm={16} md={12} lg={8} xl={10}><b>
                <Form.Item 
                    label="Password"
                    name="Password"
                    rules={[{ required: true, message: 'Please input your Password!' }]}>
                    <Password  size="large"/> 
                    </Form.Item>    
                </b></Col>
            <Col xs={2} sm={16} md={12} lg={8} xl={10}><b>
                <Form.Item 
                    label="Confirm Password"
                    name="ConfirmPassword"
                    rules={[{ required: true, message: 'Please input your Confirm Password!' }]}><Password  size="large"/></Form.Item>    
            </b></Col>  
        </Row>

        <Row gutter={8}>
            
            <Col xs={2} sm={16} md={12} lg={8} xl={10}><b>
                <Form.Item 
                    label="Contact No"
                    name="ContactNo"
                    ><Input  size="large"/></Form.Item>    
            </b></Col>  
        </Row>
        <Row>
              <Form.Item>
                   <Button onClick={onAddUser} size="large">Submit</Button>             
                   <Button  style={{ margin: '0 8px' }} onClick={props.history.goBack} size="large">Back</Button>           
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
export default AddUser;