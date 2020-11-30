import React, { useState, useEffect } from 'react';
import 'antd/dist/antd.less'
import 'antd/dist/antd.css';
import axios from '../utils/axios';
import MenuSideBar from './menuSideBar';
import {  Avatar, Skeleton } from 'antd';
import '../App';
import {
  Form,
  Input,
  Button,
  Card,
  Layout,
} from 'antd';
import Title from 'antd/lib/typography/Title';

const { Header, Content } = Layout;


const ChangePassword = props => {

  const [form] = Form.useForm()
  const [loader, setLoader] = useState(true)
  const [username,setUserData]=useState([])

  useEffect(() => {
    onDisplayUser();
  }, [])

  const onDisplayUser = async () => {
    try {
      setLoader(false);
      const response = await axios.post(`updateChangePwdUserName`);
      console.log(response.data)
      const uname=response.data
      console.log(uname.updateChangePwdUserName)
      const un=uname.updateChangePwdUserName
      setUserData(un);
    } catch (e) {
      console.log(e)
    }
  };

  const onChangePassword = async () => {
    try {

      //const validateFields = await form.validateFields()
      const values = form.getFieldsValue();
      console.log(values)
      const response = await axios.post(`updateChangePwd/${username}/${"1"}/${values.oldpassword}/${values.newpassword}/${values.confirmpassword}`);
      console.log(response.data)
    /*  if (response.data != 0) {
        alert("successfully added")
      }*/
      //props.history.push("/RoleDetails", response.data)
      //props.history.push("/RoleDetails", menuData)

      window.location.reload(false);
    } catch (e) {
      console.log(e)
    }
  };

  
  const [componentSize, setComponentSize] = useState('small');
  const onFormLayoutChange = ({ size }) => {
    setComponentSize(size);
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
                  <Form.Item label="User Name" name="userid">
                   {username}                 
                  </Form.Item>
                  <Form.Item label="Old Password" name="oldpassword" rules={[{ required: true, message: "required" }]}>
                    <Input />
                  </Form.Item>
                  <Form.Item label="New Password" name="newpassword" rules={[{ required: true, message: "required" }]} >
                    <Input />
                  </Form.Item>
                  <Form.Item label="Confirm Password" name="confirmpassword" rules={[{ required: true, message: "required" }]}>
                    <Input />
                  </Form.Item>
                  <Form.Item>
                    <Button onClick={onChangePassword}>Submit</Button>
                  </Form.Item>
                </Form>
              </Card>

             
            </Skeleton>
          </Content>
        </Layout>
      </Layout>

    </Layout>
  );
};
export default ChangePassword;