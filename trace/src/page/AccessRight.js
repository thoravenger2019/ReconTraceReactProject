import React, { useState,useEffect } from 'react';
import 'antd/dist/antd.css';
import axios from '../utils/axios';
import MenuSideBar from './menuSideBar';
import {
  Form,
  Skeleton,
  Card,
  Layout,
  Avatar,
  Table,
} from 'antd';
import Title from 'antd/lib/typography/Title';
import { Button } from 'react-bootstrap';
const {Header,Content}=Layout;

const AccessRight = props => {
  console.log(props)  
  const [selectionType, setSelectionType] = useState('checkbox');
  const [isActiveData,setIsActive]=useState([])
  const [form] = Form.useForm()
 
  const [roleAccess, setRoleAccessData] = useState([])
  const [loader, setLoader] = useState(true)
  //const [activeID,setIsActive]= useState([])
  useEffect(() => {
    onDisplayAccessRight();
  }, [])

  const onDisplayAccessRight = async () => {
    try {
       const recordAll = props.location.state.record.clientID;
       console.log(recordAll);
        const roleId = props.location.state.record.roleId;
       // const clientIdData =recordAll.clientId;
        console.log(roleId);  
      //  console.log(clientIdData); 
        const roleAccessResponse = await axios.get(`GetRoleAccessRights/${roleId}/${recordAll}`);
        console.log(roleAccessResponse.data)
        setLoader(false);
        const roleAccessR = roleAccessResponse.data;
        console.log(roleAccessR);
        const role = JSON.parse(roleAccessR.roleNames);
        console.log(role);
    const isActivedata=role.map((item,index)=>(item.isActivie));
    setIsActive(isActivedata);
    const dataAll = role.map((item, index) => ({
            menuId: item.menuID,
            Name:item.menuName.split("&nbsp;&nbsp;")[1] || item.menuName,
            isActivie: item.isActivie,
            key: index
        })
        )
        const abc=role.map((item)=>(item.isActivie));
        setIsActive(abc)
        setRoleAccessData(dataAll);
        console.log(dataAll);
    } catch (e) {
      console.log(e)
    }
  };

  const columns = [
    {
      title: 'Menu Id',
      dataIndex: 'menuId',
      key: 'index',
    },
    {
        title: 'Report Name',
        dataIndex: 'Name',
        key: 'index',
      },
  ];
    const menuData=props.location.state.menuData;
 
    console.log(isActiveData);

    if(isActiveData==1){
      
      

    }
    

    const rowSelection = {

 
    onChange:(selectedRowKeys, selectedRows) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      //const selectedRole = selectedRowKeys.data;
        console.log(selectedRows);
        const selectedList = selectedRows.map((item, index) => item.menuId)
        console.log(selectedList)
        //setData(selectedList);

      setSelectionType(selectedList);
    },
  
    getCheckboxProps: record => ({
      enabled : record.isActivie === 'Disabled User', // Column configuration not to be checked
      isActivie: record.isActivie,

    }),
   
  };
  

  const onRoleSelection = async () => {
    try {
        const roleId = props.location.state.record.roleId;
        console.log(roleId);  
        const recordAll = props.location.state.record.clientID;
        console.log(recordAll); 
      const validateFields = await form.validateFields()
      //const values = form.getFieldsValue();
      //console.log(values)
      const response = await axios.get(`AssignRoleAccessRights/${roleId}/${recordAll}/${selectionType}`);
      console.log(response.data)
      const  assignResponse=response.data;
      const assignR=JSON.parse(assignResponse.roleNames)
      if(JSON.stringify(assignR) === "[1]")
      {
        alert("role assign successfully");
      }
    
      //  props.history.push("/VendorRegistration", menuData)
        window.location.reload(false);



      } catch (e) {
      console.log(e)
    }
  };



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
      <Skeleton active={loader} loading={loader}>
      <Content>
    <Card title="Assign Role Access Rights" bordered={false} style={{ width: 800 }} >
      
    <Form  initialValues={{ remember: true }} layout={"vertical"}  form={form}>
    <Form.Item  >
    <Table dataSource={roleAccess} columns={columns}   rowSelection={{
          type: selectionType,
          ...rowSelection,
        }}/>       
    </Form.Item>
        <Form.Item>     <Button onClick={onRoleSelection}>Submit</Button></Form.Item>
   
      </Form>
      </Card>
      </Content>
      </Skeleton>
      </Layout>
      </Layout>
      </Layout>
  );
};
export default AccessRight;