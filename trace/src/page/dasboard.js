import React from 'react';
import { NavLink} from "react-router-dom";
import { Layout, Menu, Avatar } from 'antd';
import {
    DesktopOutlined,
    PieChartOutlined,   
    FileOutlined,
    TeamOutlined,
    UserOutlined,
} from '@ant-design/icons';

import SubMenu from 'antd/lib/menu/SubMenu';
import MenuSideBar from './menuSideBar';
import Title from 'antd/lib/typography/Title';

const {Header, Content}=Layout;



const Dashboard = props => {
    console.log(props)


    const menuData = props.location.state;
   
 
    const _renderSubMenuItem = (item) => {
        // <span>{(item.MenuName.split("&nbsp;&nbsp;")[1]).trim()}</span>
        const menuData = props.location.state;
        const subMenus = JSON.parse(menuData.subMenu).filter(menu => menu.ParentMenuID == item.MenuID);
        console.log("menuData",menuData)
        console.log("subMenus",subMenus)
        if (subMenus && subMenus.length > 0) {
            return <SubMenu key={item.MenuID} title={(item.MenuName.split("&nbsp;&nbsp;")[1]).trim()} icon={<FileOutlined />}>
                {subMenus.map(item => <Menu.Item key={item.MenuID,item.FilePath}>

                    <DesktopOutlined />
                    <NavLink to={item.FilePath}> <span>{(item.MenuName)}</span></NavLink>

  
                   
                </Menu.Item>)}
            </SubMenu>
        } else {
            return (<Menu.Item key={item.MenuID}>
                <DesktopOutlined />
                <span>{(item.MenuName.split("&nbsp;&nbsp;")[1]).trim()}</span>
            </Menu.Item>)
        }

    }
    const renderMenuItems = () => {

        const menudata = props.location.state;
        if (menudata && menudata.menu) {
            const menu = JSON.parse(menudata.menu);
            console.log(menu);
            return menu.map((item, i) => _renderSubMenuItem(item) )

        }

    }
    return (
    <Layout style={{height:"100%",width:"150%"}}>
        <Header style={{padding:"20px"}}>
            <Avatar shape ="square" style ={{float :"right"}} size="default" src="./max.png"/>
            <Title level={3} style={{color:"white"}}>Trace</Title>           
        </Header>
    <Layout>
            <MenuSideBar menuData={menuData}/>
            <Layout>
                <Content>
                    <div>dashboard</div>
                </Content>
        </Layout>
    </Layout>

       
    </Layout>
);
    }

export default Dashboard