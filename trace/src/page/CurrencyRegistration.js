import React, { useState, useEffect } from 'react';
import 'antd/dist/antd.less'
import 'antd/dist/antd.css';
import axios from '../utils/axios';
import MenuSideBar from './menuSideBar';
import {  Avatar, Select } from 'antd';
import '../App';
import {
  Form,
  Input,
  Button,
  Card,
  Layout,
  Space,
  Popconfirm,
  Row,
  Col,
} from 'antd';
import Title from 'antd/lib/typography/Title';
import { Table } from 'antd';

const { Option } = Select;
const { Header, Content } = Layout


/*const originData = [];

for (let i = 0; i < 10; i++) {
  originData.push({
    key: i.toString(),
    VendorType: `Network`,
    VendorName: 'NPCI',
  });
}
*/

const CurrencyRegistration = props => {
console.log(props)
  const [form] = Form.useForm()
  const [data, setData] = useState([])
  const [loader, setLoader] = useState(true)
  const [CountryData, setCountryData] = useState(true);
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
  const inputNode = dataIndex === 'Country' ?  
                                  <Select style={{ width: 250 }} onChange={onChange} disabled>
                                  {CountryData}
                                 
                                </Select>  :<Input /> 
                                      
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
        CurrencyCode: '',
        CurrencyDescription: '',
        Country: '',
        NumericCode: '',
        Scale: '',
        operation:'',

      ...record,
    });
    setEditingKey(record.key);
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
      title: 'Currency Code',
    dataIndex: 'CurrencyCode',
    width: '15%',
      editable: true,
    },
    {
      title: 'Currency Description',
    dataIndex: 'CurrencyDescription',
    width: '20%',
      editable: true,
    },
    {
       title: 'Country',
    dataIndex: 'Country',
   width: '25%',
      editable: true,
    },
    {
       title: 'Numeric Code',
    dataIndex: 'NumericCode',
   width: '15%',
      editable: true,
    },
{
       title: 'Scale',
    dataIndex: 'Scale',
   width: '12%',
      editable: true,
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
              onClick={() => save(record)}           
              style={{
                marginRight: 8,
              }}           
            >
              Update
            </a>
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
      dataIndex: 'CurrencyID',
      key:'CurrencyID',
      render: (CurrencyID,record) => (
        <Space size="middle">
          <a onClick={() => onDelete(CurrencyID)}>Delete</a>
          {console.log(record)}
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
    onDisplaycurrencyDetails();
    onDisplaycountryTypeList();
  }, [])
 const onDisplaycurrencyDetails = async () => {
   try {
      const currencyResponse = await axios.get(`currencyDetails`);
      console.log(currencyResponse.data)
      setLoader(false);

      const currencyN = currencyResponse.data;
      console.log(currencyN);
      //const vendor = JSON.parse(vendorN.roleNames);
      //console.log(role);

    const dataAll = currencyN.map((item, index) => ({
        CurrencyCode: item.CurrencyCode,
        CurrencyDescription: item.CurrencyDescription,
        Country: item.Country,
        NumericCode: item.NumericCode,
        Scale:item.Scale,
        CurrencyID:item.CurrencyID,
        ID:item.ID,
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
const onDisplaycountryTypeList = async()=> {
    try{
          const countryTypeListResponse = await axios.get(`countryTypeList`);
          console.log(countryTypeListResponse.data)
          setLoader(false);
          const countryTypeN = countryTypeListResponse.data;
          console.log(countryTypeN);
          const listCountryType=countryTypeN.map((item,index)=> <Option value={item.ID} key={index} label={item.Country}>{item.Country} </Option>)
          setCountryData(listCountryType);           
       }catch(e) {
         console.log(e)
       }
   };

const onAddCurrency = async()=> {
   try{
          const validateFields = await form.validateFields()     
          const values = form.getFieldsValue();
          console.log(values)
          const currencyResponse = await axios.get(`currencyMasterModes/${"ADD"}/${"0"}/${values.currencycode}/${values.currencydescription}/${values.countryid}/${'NULL'}/${values.numericcode}/${'NULL'}/${values.scale}`);
          console.log(currencyResponse.data)
           /* const vendorR = vendorTypeResponse.data;
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
                window.location.reload(false);*/

       }catch(e) {
         console.log(e)
       }
   };
const save = async(record)=> {
   try{
          // const menuData = props.location.state;
          const values = form.getFieldsValue();
          console.log(values)
          //const selectedValue= selectNo;
          //const finalId=selectedValue ? selectNo : record.VendorTypeID
          //console.log(finalId)
          const currencyTypeEditResponse = await axios.get(`currencyMasterModes/${"UPDATE"}/${record.CurrencyID}/${values.CurrencyCode}/${values.CurrencyDescription}/${record.ID}/${record.Country}/${values.NumericCode}/${record.Country}/${values.Scale}`);
          console.log(currencyTypeEditResponse.data)  
          /* const vendorR = vendorTypeEditResponse.data;
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
        window.location.reload();*/
       }catch(e) {
         console.log(e)
       }
   };
  const onDelete = async(CurrencyID)=> {
  try{
           //const validateFields = await form.validateFields()     
          const values = form.getFieldsValue();
          console.log(values)
          //console.log(VendorID,VendorTypeID,VendorName)
          const currencyTypeDeleteResponse = await axios.get(`currencyMasterModes/${"DELETE"}/${CurrencyID}/${"0"}/${"0"}/${"0"}/${"0"}/${"0"}/${"0"}/${"0"}`);
          console.log(currencyTypeDeleteResponse.data)  
         
        /*
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
          window.location.reload(false);*/
        
       }catch(e) {
         console.log(e)
       }
   };
  function onChange(value) {
    console.log(`selected ${value}`);
    console.log(value);
   setSelectData(value);
  }

//  const onAccess = (roleId) => {
//     /*//props.history.push("/AccessRight",roleId)
//     props.history.push("/AccessRight", { menuData, roleId })
//     //props.history.push("/AccessRight", roleId) 
//     console.log(roleId)*/
//   }

//  // const [componentSize, setComponentSize] = useState('small');
//   const onFormLayoutChange = ({ size }) => {
//     setComponentSize(size);
//   };
  
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
        
              <Card title="Currency Details" bordered={false} style={{ width: 1500 }}>
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
                    scroll={{ y: 500 }}
                    footer={() => <Form form={form}>
                    <Row layout="inline">
                            <Col flex={0.1}><b>
                              <Form.Item
                                  name="currencycode"
                                  rules={[{ required: true, message: 'Required' }]}>
                                   <Input size="large" style={{ width: 200 }} placeholder={"Enter Currency Code"}/>
                              </Form.Item>  
                            </b> </Col>
                            <Col flex={0.2} ><b>
                              <Form.Item
                                  name="currencydescription"
                                  rules={[{ required: true, message: 'Required' }]}>
                                   <Input size="large" style={{ width: 250 }}placeholder={"enter Currency Description"}/>
                              </Form.Item>  
                            </b> </Col>
                        <Col flex={0.1}><b>
                          <Form.Item 
                                name="countryid"
                                rules={[{ required: true, message: 'Required' }]}
                                >  
                                <Select size="large" placeholder={"select Country"} style={{ width: 350 }} onChange={onChange} >
                                  {CountryData}
                                </Select> 
                            </Form.Item>
                            </b></Col>  
                        <Col flex={0.1} ><b>
                          <Form.Item 
                                name="numericcode"
                                rules={[{ required: true, message: 'Required' }]}
                                >  
                                <Input size="large" style={{ width: 200 }} placeholder={"Enter Numeric Code"}/>
                          </Form.Item>
                        </b></Col>
                        <Col flex={0.1}><b>
                          <Form.Item 
                                name="scale"
                                rules={[{ required: true, message: 'Required' }]}
                                >  
                                <Input size="large" style={{ width: 200 }} placeholder={"Enter Scale"}/>
                          </Form.Item>
                        </b></Col>  
                            
                        <Col flex={0.1}><b>
                          <Form.Item>
                              <Button type="primary" size="large" onClick={onAddCurrency}>Add New</Button>                                                    
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

          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};
export default CurrencyRegistration;













































