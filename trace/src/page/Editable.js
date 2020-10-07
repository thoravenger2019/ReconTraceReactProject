import React, { useContext, useState, useEffect, useRef } from 'react';
import { Table, Input, Button, Popconfirm, Form,Select } from 'antd';
import axios, { axiosGet } from '../utils/axios';
const { Option } = Select;
const EditableContext = React.createContext();

const EditableRow = ({ index, ...props }) => {
  const [form] = Form.useForm();
  return (
    <Form form={form} component={false}>
      <EditableContext.Provider value={form}>
        <tr {...props} />
      </EditableContext.Provider>
    </Form>
  );
};

const EditableCell = ({
  title,
  editable,
  children,
  dataIndex,
  record,
  handleSave,
  ...restProps
}) => {
  const [editing, setEditing] = useState(false);
  const [vendorData, setVendorData] = useState(true);
  const [loader, setLoader] = useState(true)

  const inputRef = useRef();
  const form = useContext(EditableContext);
  useEffect(() => {
    //onDisplayVentorType();
    if (editing) {
      inputRef.current.focus();
    }
  }, [editing]);

  const toggleEdit = () => {
    setEditing(!editing);
    form.setFieldsValue({
      [dataIndex]: record[dataIndex],
    });
  };

/*  const onDisplayVentorType = async()=> {
    try{
          const vendorTypeResponse = await axios.get(`vendorTypeList`);
          console.log(vendorTypeResponse.data)
          setLoader(false);

          const vendorTypeN = vendorTypeResponse.data;
          console.log(vendorTypeN);
          const listVendorType=vendorTypeN.map((item,index)=> <Option value={item.id} key={index} label={item.channeltype}>{item.channeltype}</Option>)
          setVendorData(listVendorType);
             
       }catch(e) {
         console.log(e)
       }
   };
*/
  const save = async e => {
    try {
      const values = await form.validateFields();
      toggleEdit();
      handleSave({ ...record, ...values });
    } catch (errInfo) {
      console.log('Save failed:', errInfo);
    }
  };

  let childNode = children;

  if (editable) {
    childNode = editing ? (
      <Form.Item
        style={{
          margin: 0,
        }}
        name={dataIndex}
        rules={[
          {
            required: true,
            message: `${title} is required.`,
          },
        ]}
      >
        <Input ref={inputRef} onPressEnter={save} onBlur={save} />
      </Form.Item>
    ) : (
      <div
        className="editable-cell-value-wrap"
        style={{
          paddingRight: 24,
        }}
        onClick={toggleEdit}
      >
        {children}
      </div>
    );
  }

  return <td {...restProps}>{childNode}</td>;
};



export default class EditableTable extends React.Component {
  constructor(props) {
    super(props);
    this.columns = [
      {
        title: 'Channel Type',
        dataIndex: 'ChannelType',
        width: '30%',
        editable: true,
      },
      {
        title: 'Onus',
        dataIndex: 'Onus',
      },
      {
        title: 'Acqiurer',
        dataIndex: 'Acqiurer',
      },
      {
        title: 'issuer',
        dataIndex: 'issuer',
      },
      {
        title: 'operation',
        dataIndex: 'operation',
        render: (text, record) =>
          this.state.dataSource.length >= 1 ? (
            <Popconfirm title="Sure to delete?" onConfirm={() => this.handleDelete(record.key)}>
              <a>Delete</a>
            </Popconfirm>
          ) : null,
      },
    ];
    this.state = {
      dataSource: [
        {
          key: '0',
          ChannelType: <Select style={{width:'45%'}} defaultValue={"--select--"} onChange={this.onChangeChannel}></Select>,
          age: '32',
          address: 'London, Park Lane no. 0',
        },
      ],
      count: 2,
    };
    
  }

  componentDidMount = () => {
    //const that = this;   
        axios
        .get("http://localhost:8080/Admin/api/vendorTypeList")
        .then(function(response) {
            this.setState({Posts : response.data});
            console.log(response.data)
        }.bind(this))  // <-- notice the .bind(this)
        .catch(function(error) {
            console.log(error);
        });
    }
  
  handleDelete = key => {
    const dataSource = [...this.state.dataSource];
    this.setState({
      dataSource: dataSource.filter(item => item.key !== key),
    });
  };

  handleAdd = () => {
    const { count, dataSource } = this.state;
    const newData = {
      key: count,
      name: `Edward King ${count}`,
      age: 32,
      address: `London, Park Lane no. ${count}`,
    };
    this.setState({
      dataSource: [...dataSource, newData],
      count: count + 1,
    });
  };

  handleSave = row => {
    const newData = [...this.state.dataSource];
    const index = newData.findIndex(item => row.key === item.key);
    const item = newData[index];
    newData.splice(index, 1, { ...item, ...row });
    this.setState({
      dataSource: newData,
    });
  };

  render() {
    const { dataSource } = this.state;
    const components = {
      body: {
        row: EditableRow,
        cell: EditableCell,
      },
    };
    const columns = this.columns.map(col => {
      if (!col.editable) {
        return col;
      }

      return {
        ...col,
        onCell: record => ({
          record,
          editable: col.editable,
          dataIndex: col.dataIndex,
          title: col.title,
          handleSave: this.handleSave,
        }),
      };
    });
    return (
      <div>
        <Button
          onClick={this.handleAdd}
          type="primary"
          style={{
            marginBottom: 16,
          }}
        >
          Add a row
        </Button>
        <Table
          components={components}
          rowClassName={() => 'editable-row'}
          bordered
          dataSource={dataSource}
          columns={columns}
        />
      </div>
    );
  }
}
