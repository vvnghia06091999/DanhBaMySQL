package com.example.danhba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String DB_URL = "jdbc:mysql://localhost:3306/holoandroid";
    private static String USER_NAME = "root";
    private static String PASSWORD = "060999";
    private List<User> userList = new ArrayList<User>();
    private UserAdapter userAdapter;
    private String id;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList("1");
       // init();
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(userList,this);
        recyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }

    private void initList(String id){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(DB_URL,USER_NAME,PASSWORD);
            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery("select * from User u join Relationship r" +
//                    " on u.id = r.id_user_1 or u.id = r.id_user_2 " +
//                    " where ( id_user_1 = '"+id+"' or id_user_2 = '"+id+"' ) and u.id != '"+id+"'" +
//                    "  group by id;");
//            ResultSet rs = stmt.executeQuery("select u.id,u.name from user u join relationship r\r\n" +
//                    "		on u.id = r.id_user_1 or u.id = r.id_user_2\r\n" +
//                    "        where ( id_user_1 = '1' or id_user_2 = '1' ) and u.id != '1' and status = 2\r\n" +
//                    "        group by name");
            ResultSet rs = stmt.executeQuery("select * from user");
            while (rs.next()){
                String iduser = rs.getString(1);
                String name = rs.getString(2);
                String soDienThoai = rs.getString(3);
                String email = rs.getString(4);
                String ngaySinh = rs.getString(5);
                String gioiTinh = rs.getString(7);
                User user = new User(iduser,name,soDienThoai,email,ngaySinh,gioiTinh);
                userList.add(user);
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    private void init(){
        User user = new User("1","XXX","aa","aa","aa","a");
        userList.add(user);
        user = new User("1","XXX","aa","aa","aa","a");
        userList.add(user);
        user = new User("2","XXX","aa","aa","aa","a");
        userList.add(user);
        user = new User("3","XXX","aa","aa","aa","a");
        userList.add(user);
    }
}