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
  Space,
  InputNumber,
  Popconfirm,
  Row,
  Col,
  Typography,
} from 'antd';
import Title from 'antd/lib/typography/Title';
import { Table } from 'antd';
import {
  UserSwitchOutlined,
  EditOutlined,
  DeleteOutlined,
} from '@ant-design/icons';
import { blue } from '@ant-design/colors';
const FormItem = Form.Item;
const { Option } = Select;
const { Header, Footer, Sider, Content } = Layout;



/*const originData = [];

for (let i = 0; i < 10; i++) {
  originData.push({
    key: i.toString(),
    VendorType: `Network`,
    VendorName: 'NPCI',
  });
}
*/

const VendorRegistration = props => {
console.log(props)
  const [form] = Form.useForm()
  const [data, setData] = useState([])
  const [loader, setLoader] = useState(true)
  const [vendorData, setVendorData] = useState(true);
   const [selectNo, setSelectData] = useState('');
  const [editingKey, setEditingKey] = useState('');
  const isEditing = record => record.key === editingKey;
  
console.log(selectNo);
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
  const inputNode = dataIndex === 'VendorName' ? <Input /> : 
                                  <Select style={{ width: 250 }} onChange={onChange}>
                                  {vendorData}
                                </Select>  
                                      
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
  const edit = record => {
    form.setFieldsValue({
        VendorType: '',
        VendorName: '',
        Action: '',
      ...record,
    });
    setEditingKey(record.key);
    alert("record key"+ record.key);
  };

  const cancel = () => {
    setEditingKey('');
  };

/*  const save = async key => {
    try {
     /* const row = await form.validateFields();
      const newData = [...data];
      const index = newData.findIndex(item => key === item.key);

      if (index > -1) {
        const item = newData[index];
        newData.splice(index, 1, { ...item, ...row });
        setData(newData);
        setEditingKey('');
      } else {
        newData.push(row);
        setData(newData);
        setEditingKey('');
      }*/

         /* const vendorTypeResponse = await axios.get(`vendorMasterModes/${"ADD"}/${"11"}/${values.VendorTypeName}/${values.vendorId}`);
          console.log(vendorTypeResponse.data)
          setLoader(false);
         
    } catch (errInfo) {
      console.log('Validate Failed:', errInfo);
    }
  };*/

  const columns = [
    {
      title: 'Vendor Type',
      dataIndex: 'VendorType',
      width: '25%',
      color:'blue',
      editable: true,
      render: (VendorType, record) => (
      <Typography.Text style={{ fontSize: record.size }}>
        {VendorType}
      </Typography.Text>
    )
    },
    {
      title: 'Vendor Name',
      dataIndex: 'VendorName',
      width: '35%',
      color:'blue',
      editable: true,
       render: (VendorName, record) => (
      <Typography.Text style={{ fontSize: record.size }}>
        {VendorName}
      </Typography.Text>
    )
    },
    {
      title: 'operation',
      dataIndex: 'VendorID',
      key:'VendorID',
      render: (VendorID, record) => {
     
      const editable = isEditing(record);
        return editable ? (
          <span>
            <a
              href="#!;"
              onClick={() => save(VendorID,record)}
              
              style={{
                marginRight: 8,
              }}
              
            >
              Update
            </a>
            {console.log(VendorID)}
            {console.log(record)}
            <Popconfirm title="Sure to cancel?" onConfirm={cancel}>
              <a>Cancel</a>
            </Popconfirm>
          </span>
        ) : (
          <a disabled={editingKey !== ''} onClick={() => edit(record)}>
            Edit
          </a>
          ); 
      },      
    },
    {
      title: 'Action',
      dataIndex: 'VendorID',
      key:'VendorID',
      render: (VendorID,record) => (
        <Space size="middle">
          <a onClick={() => onDelete(VendorID)}>Delete</a>
        </Space>
      ),
    },
  ];
  const mergedColumns = columns.map(col => {
    if (!col.editable) {
      return col;
    }

    return {
      ...col,
      onCell: record => ({
        record,
       // inputType: col.dataIndex === 'age' ? 'number' : 'text',
        dataIndex: col.dataIndex,
        title: col.title,
        editing: isEditing(record),       
      }),
    };
  });
  
  useEffect(() => {
    onDisplayVendorDetails();
    onDisplayVentorType();
  }, [])

  const onDisplayVendorDetails = async () => {
      try {
    const vendorResponse = await axios.get(`vendorDetails`);
      console.log(vendorResponse.data)
      setLoader(false);

      const vendorN = vendorResponse.data;
      console.log(vendorN);
     // const vendor = JSON.parse(vendorN.roleNames);
      //console.log(role);

    const dataAll = vendorN.map((item, index) => ({
        VendorType: item.VendorType,
        VendorTypeID: item.VendorTypeID,
        VendorName: item.VendorName,
        VendorID:item.VendorID,
        key: index,
        size: '15px'
      })
      )
      setData(dataAll);

      console.log(dataAll);

    } catch (e) {
      console.log(e)
    }
  };
const onDisplayVentorType = async()=> {
    try{
          const vendorTypeResponse = await axios.get(`vendorTypeList`);
          console.log(vendorTypeResponse.data)
          setLoader(false);

          const vendorTypeN = vendorTypeResponse.data;
          console.log(vendorTypeN);
          const listVendorType=vendorTypeN.map((item,index)=> <Option value={item.id} key={index} label={item.vendorType}>{item.vendorType}</Option>)
          setVendorData(listVendorType);
             
       }catch(e) {
         console.log(e)
       }
   };

const onAddVendor = async()=> {
    try{
          const validateFields = await form.validateFields()     
          const values = form.getFieldsValue();
          console.log(values)
          const vendorTypeResponse = await axios.get(`vendorMasterModes/${"ADD"}/${"15"}/${values.VendorTypeName}/${values.vendorId}`);
          console.log(vendorTypeResponse.data)
          const vendorR = vendorTypeResponse.data;
          console.log(vendorR);
          const vendor = JSON.parse(vendorR.vendorMasterModes);
          console.log(vendor)
          console.log(JSON.stringify(vendorTypeResponse.data));
             if(JSON.stringify(vendor) === "[1]")
              {
                alert("Vendor added successfully");
              }
              else if(JSON.stringify(vendor) === "[0]")
              {
                 alert(" vendor already Exist");
              }
                props.history.push("/VendorRegistration", menuData)
                window.location.reload(false);

       }catch(e) {
         console.log(e)
       }
   };
const save = async(VendorID,record)=> {
    try{
          // const menuData = props.location.state;
          const values = form.getFieldsValue();
          console.log(values)
          const selectedValue= selectNo;
          const finalId=selectedValue ? selectNo : record.VendorTypeID
          console.log(finalId)
          const vendorTypeEditResponse = await axios.get(`vendorMasterModes/${"UPDATE"}/${VendorID}/${values.VendorName}/${finalId}`);
          console.log(vendorTypeEditResponse.data)  
          const vendorR = vendorTypeEditResponse.data;
          console.log(vendorR);
          const vendor = JSON.parse(vendorR.vendorMasterModes);
          console.log(vendor)
              if(JSON.stringify(vendor) === "[1]")
              {
                alert("Vendor Updated successfully");
              }
              else if(JSON.stringify(vendor) === "[0]")
              {
                alert(" vendor already Exist");
              }
        props.history.push("/VendorRegistration", menuData)
        window.location.reload();
       }catch(e) {
         console.log(e)
       }
   };
  const onDelete = async(VendorID,VendorTypeID,VendorName)=> {
    try{
           //const validateFields = await form.validateFields()     
          const values = form.getFieldsValue();
          console.log(values)
          console.log(VendorID,VendorTypeID,VendorName)
          const vendorTypeDeleteResponse = await axios.get(`vendorMasterModes/${"DELETE"}/${VendorID}/${0}/${0}`);
          console.log(vendorTypeDeleteResponse.data)
          const vendorR = vendorTypeDeleteResponse.data;
          console.log(vendorR);
          const vendor = JSON.parse(vendorR.vendorMasterModes);
          console.log(vendor)
              if(JSON.stringify(vendor) === "[1]")
              {
                alert("Vendor Deleted successfully");
              }
          props.history.push("/VendorRegistration", menuData)
          window.location.reload(false);
        
       }catch(e) {
         console.log(e)
       }
   };
  function onChange(value) {
    console.log(`selected ${value}`);
    console.log(value);
   setSelectData(value);
  }

 const onAccess = (roleId) => {
    /*//props.history.push("/AccessRight",roleId)
    props.history.push("/AccessRight", { menuData, roleId })
    //props.history.push("/AccessRight", roleId) 
    console.log(roleId)*/
  }

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
              <Card title="Vendor Details" bordered={false} style={{ width: 1500 }}>
              <Form form={form} component={false}>
              <Table
                    components={{
                      body: {
                        cell: EditableCell,
                         
                      },
                    }}                    
                    bordered
                    dataSource={data}
                    columns={mergedColumns}         
                    size="middle"          
                    footer={() => <Form form={form}>
                    <Row gutter={8} layout="inline">
                            <Col flex={1}><b>
                              <Form.Item
                                  name="vendorId"
                                  rules={[{ required: true, message: 'Required' }]}>
                                  <Select defaultValue="--Select ventor Type--" style={{ width: 250 }} onChange={onChange}>
                                  {vendorData}
                                </Select>  
                              </Form.Item>  
                            </b> </Col>
                        <Col flex={1}><b>
                          <Form.Item 
                                label="Vendor Type Name"
                                name="VendorTypeName"
                                rules={[{ required: true, message: 'Required' }]}
                                >  
                                <Input placeholder={"Enter Vendor Type Name"}/>
                            </Form.Item>
                            </b></Col>
                        <Col flex={1}><b>
                          <Form.Item>
                              <Button onClick={onAddVendor}>Add New</Button>             
                                        
                          </Form.Item> 
                            </b></Col>
                        
                            </Row>
                            </Form>
                    }
                    pagination={{
                      onChange: cancel,
                    }}
                  />
                </Form>
              </Card>
            </Skeleton>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};
export default VendorRegistration;