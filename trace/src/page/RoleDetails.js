import React, { useState, useEffect } from 'react';
import 'antd/dist/antd.less'
import 'antd/dist/antd.css';
import axios, { axiosGet } from '../utils/axios';
import Axios from "axios";
import MenuSideBar from './menuSideBar';
import { NavLink } from "react-router-dom";
import { Spin, Avatar, Skeleton,Select } from 'antd';
import '../App';
import {
  Form,
  Input,
  Button,
  Card,
  Layout,
  Space
} from 'antd';
import Title from 'antd/lib/typography/Title';
import { Table } from 'antd';
import {
  UserSwitchOutlined,
  EditOutlined,
  DeleteOutlined,
} from '@ant-design/icons';

const { Option } = Select;
const { Header, Footer, Sider, Content } = Layout;

let TERA;
const RoleDetails = props => {

  const [form] = Form.useForm()
  const [data, setData] = useState([])
  const [loader, setLoader] = useState(true)
  const [clientdata, setClientData] = useState([])
  const [clientID,setClientId]=useState([])

  useEffect(() => {
    ///onDisplayRole();
    onDisplayClientNameList();
  }, [])

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
  const onDisplayRole = async (value) => {
    try {
      const roleResponse = await axios.get(`GetRoleDetails/${value}`);
      console.log(roleResponse.data)
      setLoader(false);

      const roleN = roleResponse.data;
      console.log(roleN);
      const role = JSON.parse(roleN.roleNames);
      console.log(role);

      const dataAll = role.map((item, index) => ({
        Name: item.roleNAme,
        roleId: item.roleID,
        clientID: item.clientID,
        key: index
      })
      )
      setData(dataAll);

      console.log(dataAll)
    } catch (e) {
      console.log(e)
    }
  };

  const columns = [{

    title: 'Role Names',
    dataIndex: 'Name',
    key: 'Name',
    render: text => <a>{text}</a>,
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
    title: 'Action',
    dataIndex: 'action',
    key: 'delete',
    render: (text, record) => (
      <Space size="middle">
        <DeleteOutlined />
      </Space>
    ),
  }];

  // props.history.push("/AccessRight", menuData)
  function onChange(value) {
    console.log(`selected ${value}`);
    //setClientId(value)
  
    onDisplayRole(value)
   
  }
  const onAccess = (record) => {
    //props.history.push("/AccessRight",roleId)
    props.history.push("/AccessRight", { menuData, record })
    //props.history.push("/AccessRight", roleId) 
    console.log(record)
  }
  const addRole = async () => {
    try {
      const validateFields = await form.validateFields()
      const values = form.getFieldsValue();
      console.log(values)
      const response = await axios.get(`GetRoleMaster/${values.roleName}/${values.homePage}/${"ADD"}/${"1"}/${values.clientID}`);
      console.log(response.data)
      if (response.data != 0) {
        alert("successfully added")
      }
      props.history.push("/RoleDetails", response.data)
      props.history.push("/RoleDetails", menuData)

      window.location.reload(false);
    } catch (e) {
      console.log(e)
    }
  };
  const [componentSize, setComponentSize] = useState('small');
  const onFormLayoutChange = ({ size }) => {
    setComponentSize(size);
  };
  const tailLayout = {
    wrapperCol: { offset: 10 },
  };

  const menuData = props.location.state;

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
            <Skeleton active={loader} loading={loader}>
              <Card title="Role Details" bordered={false} style={{ width: 800 }}>

                <Form
                  labelCol={{
                    span: 4,
                  }}
                  wrapperCol={{
                    span: 5,
                  }}
                  layout="vertical"
                  initialValues={{

                    size: componentSize
                  }}
                  onValuesChange={onFormLayoutChange}
                  size={componentSize}
                  form={form}
                >
                  <Form.Item label="Client Name" name="clientID" >
                    <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChange}>
                     {clientdata}
                    </Select>                     
                  </Form.Item>
                  <Form.Item label="Role Name" name="roleName" rules={[{ required: true, message: "Role Name required" }]}>
                    <Input />
                  </Form.Item>
                  <Form.Item label="Default Page" name="homePage" >
                    <Input />
                  </Form.Item>
                  <Form.Item>
                    <Button onClick={addRole}>Submit</Button>
                  </Form.Item>
                </Form>
              </Card>
              <Card>
                {loader ? ("loading..") : (
                  <Table columns={columns}
                    dataSource={data} scroll={{ y: 240 }} bordered />
                )}
              </Card>
            </Skeleton>
          </Content>
        </Layout>
      </Layout>

    </Layout>
  );
};
export default RoleDetails;