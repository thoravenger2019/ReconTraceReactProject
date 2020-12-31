import React, { useState, useEffect } from 'react';
import 'antd/dist/antd.css';
//import { Button } from 'react-bootstrap';
import axios from '../utils/axios';
import {
  Form,
  Input,
  Card,
  Layout,
  Avatar,
  Select,
  Skeleton,
  Space,
  Checkbox,
  Table,
  Button,
} from 'antd';
import MenuSideBar from './menuSideBar';
import Title from 'antd/lib/typography/Title';
import {
  UserSwitchOutlined,
  EditOutlined,
  DeleteOutlined,
} from '@ant-design/icons';
const { Header,  Content } = Layout;
const UserDetails = (props) => {

  const [roledata, setRoleData] = useState([])
  const [branchdata, setBranchData] = useState([])
  const [loader, setLoader] = useState(true)
  const [clientdata, setClientData] = useState([])
  const [clientIDData, setClientID] = useState([])
  const [roleID,setRoleID]=useState([])
  const [data,setData]=useState([])
  const [selectionType, setSelectionType] = useState('checkbox');

  console.log(clientIDData)
  useEffect(() => {
    onDisplayClientNameList();
  }, [])

  const { Option } = Select;
  const onDisplayClientNameList = async () => {
    try {
        const clientNameResponse = await axios.get(`clientName`);
        console.log(clientNameResponse.data)
        setLoader(false);
        const clientNameN = clientNameResponse.data;
        console.log(clientNameN);

    //   setClientID()
        //const role = JSON.parse(roleN.roleNames);
        //console.log(role);
      //  const firstClient=clientNameN[0];
        const clientNameList =clientNameN.map((item, index) =>
        <Option value={item.id} key={index}>{item.clientNameList}
        </Option>
        )




        setClientData(clientNameList);
       // onDisplayUserRole();
    } catch (e) {
        console.log(e)
    }
};

  const onDisplayUserRole = async (value) => {
    try {
      console.log(value)
      const roleResponse = await axios.get(`GetRoleDetails/${value}`);
      console.log(roleResponse.data)
      setLoader(false);
      const roleN = roleResponse.data;
      console.log(roleN);
      const role = JSON.parse(roleN.roleNames);
      console.log(role);
      const listFile = role.map((item, index) => <Option value={item.roleID} key={index}>{item.roleNAme}</Option>)
      setRoleData(listFile);
    } catch (e) {
      console.log(e)
    }
  };

  const onDisplayBranch = async (value) => {
    try {
      const branchResponse = await axios.get(`GetBranchDetails/${value}/${"0"}`);
      console.log(branchResponse.data)
      setLoader(false);
      const branchN = branchResponse.data;
      console.log(branchN);
      const branch = JSON.parse(branchN.branchName);
      console.log(branch);
      const listBranch = branch.map((item, index) => <Option value={item.branchCode} key={index}>{item.branchName}</Option>)
      setBranchData(listBranch);
     } catch (e) {
      console.log(e)
    }
  };

  const getUser = async () => {
    try {
     // const validateFields = await form.validateFields()
      const values = form.getFieldsValue();
      console.log(values)
      const response = await axios.get(`GetUserDetails/${values.clientID}/${values.branchName}/${values.roleType}`);
      console.log(response.data)
      const userN = response.data;
     // console.log(roleN);
      const userList = JSON.parse(userN.getUser);
    //  console.log(role);

      const dataAll = userList.map((item, index) => ({
        ClientName: item.clientName,
        userID: item.userID,
        roleName: item.roleName,
        BranchName: item.BranchName,
        branchcode: item.branchcode,
        EmailID: item.EmailID,
        key: index
      })
      )
      setData(dataAll);

      } catch (e) {
      console.log(e)
    }
  };

  const columns = [{

    title: 'Client Name',
    dataIndex: 'ClientName',
    key: 'ClientName',
    render: text => <a>{text}</a>,
  },
  {
    title: 'userID',
    dataIndex: 'userID',
    key: 'userID',
  },
  {
    title: 'roleName',
    dataIndex: 'roleName',
    key: 'roleName',
   
  },
  {
    title: 'Branch Name',
    dataIndex: 'BranchName',
    key: 'BranchName',
    
  },
  {
    title: 'Email ID',
    dataIndex: 'EmailID',
    key: 'EmailID',
    
  },
  {
    title: 'Branch code',
    dataIndex: 'branchcode',
    key: 'branchcode',
    
  },
  {
    title: 'Account Status',
    dataIndex: 'status',
    key: 'status',
    render: (text, record) => (
      <Space size="middle">
        <Checkbox onChange={onChange}></Checkbox>
      </Space>
    ),
  },
  {
    title: 'Access',
    dataIndex: 'roleId',
    key: 'roleId',
    render: (roleID,record) => (
      <Space size="middle">
        <a onClick={() => { onAccess(record) }}><UserSwitchOutlined /></a>
        {console.log(record)}
      </Space>
    ),
  },
  {
    title: 'Update',
    dataIndex: 'update',
    key: 'update',
    render: (text, record) => (
      <Space size="middle">
        <a><EditOutlined /></a>
      </Space>
    ),
  },
  {
    title: 'Delete',
    dataIndex: 'action',
    key: 'delete',
    render: (text, record) => (
      <Space size="middle">
        <DeleteOutlined />
      </Space>
    ),
  }];
  const onAccess = (record) => {
    //props.history.push("/AccessRight",roleId)
    props.history.push("/AccessRight", { menuData, record })
    //props.history.push("/AccessRight", roleId) 
    console.log(record)
  }
  const [componentSize, setComponentSize] = useState('small');
  const onFormLayoutChange = ({ size }) => {
    setComponentSize(size);
  };
  const tailLayout = {
    wrapperCol: { offset: 10 },
  };
  const menuData = props.location.state;
  const addUser = () => {
    props.history.push("/addUser", menuData)
  }

  function onChange(value) {
    console.log(`selected ${value}`);
    setClientID(value)
    onDisplayUserRole(value)
  }

  function onChangeRole(value) {
    console.log(`selected ${value}`);
    setRoleID(value)
    onDisplayBranch(value)
  }
  const [form] = Form.useForm()
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
      disabled: record.name === 'Disabled User', // Column configuration not to be checked
      name: record.name,

    }),
   
  };
  


  return (

    <Layout style={{ height: "100%" }}>

      <Header style={{ padding: "20px" }}>
        <Avatar shape="square" style={{ float: "right" }} size="default" src="./max.png" />
        <Title level={3} style={{ color: "white" }}>Trace</Title>
      </Header>

      <Layout>
        <MenuSideBar menuData={menuData} />
        <Layout style={{ height: "100vh", backgroundColor: "white" }}>
          <Content>
            <Skeleton active={loader} loading={loader}>
              <Card title="User Details" bordered={false} style={{ width: 800 }}>
                <Form
                  
                  layout="vertical"
                 

                 
                  form={form}
                >
                  <Form.Item label="Client Name" name="clientID" >
                    <Select  style={{ width: 400 }} onChange={onChange} size="large">
                         {clientdata}
                    </Select>                     
                  </Form.Item>
                  <Form.Item label="Role Type" name="roleType">
                    <Select onChange={onChangeRole} size="large" style={{width:400}} >
                      {roledata}
                    </Select>
                  </Form.Item>
                  <Form.Item label="Branch Name" name="branchName">
                    <Select size="large" style={{width:400}}>
                      {branchdata}
                    </Select>
                  </Form.Item>
                  <Form.Item label="User ID" name="UserId">
                    <Input size="large" style={{width:400}} />
                  </Form.Item>
                  <Form.Item >
                  <Button  style={{backgroundColor:'#52c41a'}} onClick={getUser} size="large" >Search</Button>
                  <Button  style={{ margin: '0 10px'}} onClick={addUser} size="large" type={"primary"}>Add User</Button>
                  </Form.Item> 
                  
                </Form>
                <Table dataSource={data} columns={columns}   rowSelection={{
                  type: selectionType,
                  ...rowSelection,
                }}/>    
              </Card>
            </Skeleton>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};
export default UserDetails;