package com.itwillbs.member.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	
	Connection con=null;
	PreparedStatement pstmt=null;
	PreparedStatement pstmt2=null;
	ResultSet rs=null;
	
	// 디비연결 메서드
	public Connection getConnection() throws Exception {
		Context init=new InitialContext();
		DataSource ds= (DataSource)init.lookup("java:comp/env/jdbc/Mysql");
		con=ds.getConnection();
		return con;
	} // 디비연결 메서드
	
	// close 메서드
	public void close() {
		if(con!=null)try{con.close();} catch(SQLException e) {}
		if(pstmt!=null)try{pstmt.close();} catch(SQLException e) {}
		if(pstmt2!=null)try{pstmt2.close();}catch(SQLException ex){}
		if(rs!=null)try{rs.close();} catch(SQLException e) {}
	} // close 메서드
	
	//아이디중복체크 메서드 insertIdCheck
	public int insertIdCheck(String cus_id){
		int result = -1;
		try {
			con= getConnection();
			String sql = "select cus_id from customer where cus_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, cus_id);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()){result = 0;} 
			else{result = 1;}

		} catch (Exception e) { e.printStackTrace(); } finally { close(); }
		
		return result;
	}//insertIdCheck
		
	// insertMember 메서드
	public void insertMember(MemberDTO dto) {
		
		try {
			con= getConnection();
			String sql2 = "select max(cus_num) as cus_num from customer";
			pstmt2 = con.prepareStatement(sql2);
			rs = pstmt2.executeQuery();
			int num = 0;
			if(rs.next()) {
				num= rs.getInt("cus_num")+1;
			}
			Connection con=getConnection();
			String sql="insert into customer(cus_num,cus_id,cus_pass,cus_name,cus_phone,cus_email,cus_birth) values(?,?,?,?,?,?,?)";
			PreparedStatement pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, dto.getCus_id());
			pstmt.setString(3, dto.getCus_pass());
			pstmt.setString(4, dto.getCus_name());
			pstmt.setString(5, dto.getCus_phone());
			pstmt.setString(6, dto.getCus_email());
			pstmt.setString(7, dto.getCus_birth());
			
			pstmt.executeUpdate(); //sql insert,update,delete 때 사용
			
		} catch (Exception e) { e.printStackTrace(); } finally { close(); }
	
	} // insertMember 메서드
	
	// 카카오 insertMember 메서드
	public void KinsertMember(MemberDTO dto) {
		
		try {
			con= getConnection();
			String sql2 = "select max(cus_num) as cus_num from customer";
			pstmt2 = con.prepareStatement(sql2);
			rs = pstmt2.executeQuery();
			
			int num = 0;
			if(rs.next()) {
				num= rs.getInt("cus_num")+1;
			}
			
			Connection con=getConnection();
			String sql="insert into customer(cus_num,cus_id,cus_pass,cus_name,cus_email) values(?,?,?,?,?)";
			PreparedStatement pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, dto.getCus_id());
			pstmt.setString(3, dto.getCus_id());
			pstmt.setString(4, dto.getCus_name());
			pstmt.setString(5, dto.getCus_email());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) { e.printStackTrace(); } finally { close(); }
	} // 카카오 KinsertMember 메서드 닫음
	
	// userCheck()
	public MemberDTO userCheck(String cus_id, String cus_pass) {
		MemberDTO dto=null;
		
		try {
			Connection con=getConnection();
			String sql="select * from customer where cus_id=? and cus_pass=?";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setString(1, cus_id);
			pstmt.setString(2, cus_pass);
			
			ResultSet rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new MemberDTO();
				dto.setCus_id(rs.getString("cus_id"));
				dto.setCus_pass(rs.getString("cus_pass"));
				dto.setCus_name(rs.getString("cus_name"));
			} else {}
			
		} catch (Exception e) {e.printStackTrace();} finally {close();}
		return dto;
	} // userCheck()
	
	// 멤버정보조회 메서드
	public MemberDTO getMember(String cus_id) {
		MemberDTO dto=null;
		
		try {
			con=getConnection();
			String sql="select * from customer where cus_id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, cus_id);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new MemberDTO();
				dto.setCus_num(rs.getInt("cus_num"));
				dto.setCus_id(rs.getString("cus_id"));
				dto.setCus_pass(rs.getString("cus_pass"));
				dto.setCus_name(rs.getString("cus_name"));
				dto.setCus_phone(rs.getString("cus_phone"));
				dto.setCus_email(rs.getString("cus_email"));
				dto.setCus_birth(rs.getString("cus_birth"));
				
			} else {}
		} catch (Exception e) {e.printStackTrace();} finally {close();}
		return dto;
	} // 멤버정보조회 메서드
	
	//updateMember()
	public void updateMember(MemberDTO dtoUpdate) {
		
		try {
			con=getConnection();
			String sql="update customer set cus_name=?,cus_phone=?,cus_email=?,cus_birth=? where cus_id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, dtoUpdate.getCus_name());
			pstmt.setString(2, dtoUpdate.getCus_phone());
			pstmt.setString(3, dtoUpdate.getCus_email());
			pstmt.setString(4, dtoUpdate.getCus_birth());
			pstmt.setString(5, dtoUpdate.getCus_id());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {e.printStackTrace();} finally {close();}
	} //updateMember()
	
	// 멤버 삭제
	public void deleteMember(String cus_id) {
		
		try {
			con=getConnection();
			String sql="delete from customer where cus_id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, cus_id);
			pstmt.executeUpdate();
			
		} catch (Exception e) {e.printStackTrace();} finally {close();}
	} // 멤버 삭제
	
	// 멤버 목록 조회
	public List getMemberList() {
		List<MemberDTO> memberList=new ArrayList<MemberDTO>();
		
		try {
			con=getConnection();
			String sql="select * from customer";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				MemberDTO dto=new MemberDTO();
				dto.setCus_id(sql);
				dto.setCus_pass(rs.getString("cus_pass"));
				dto.setCus_name(rs.getString("cus_name"));
				
				memberList.add(dto);
			}
		} catch (Exception e) {e.printStackTrace();} finally {close();}
		return memberList;
	} // 멤버 목록 조회
	
}
