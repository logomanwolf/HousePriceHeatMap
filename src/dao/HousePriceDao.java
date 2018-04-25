package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.House;
import bean.Position;

public class HousePriceDao extends BaseDao {

	//用于选择当前的房子
	public List<House> selectHouse() {

		String sql = "select * from house where id>1000 and id<1100";
		List<House> result = new ArrayList<House>();
		ResultSet rs = this.executeQuery(sql, null);
		try {
			while (rs.next()) {
				House house = new House();
				house.setId(rs.getInt("id"));
				house.setAddr(rs.getString("addr"));
				house.setPrice(rs.getLong("aver"));
				result.add(house);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
		return result;
	}

	
	//用于得到所有的经纬度信息
	public List<Position> selectUsableHouse() {
		String sql="SELECT DISTINCT house.lng, house.lat,house.aver FROM fivecrowdsourcing.house WHERE house.lng > 0;";
		List<Position> result = new ArrayList<Position>();
		ResultSet rs = this.executeQuery(sql, null);
		try {
			while (rs.next()) {
				Position po=new Position();
				po.setLng(rs.getString("lng"));
				po.setLat(rs.getString("lat"));
				po.setCount(rs.getString("aver"));
				result.add(po);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
		return result;
	}
	
	//用于更新房子的经纬度
	public Integer updateHouses(List<House> houses) {
		String sql = "";
		List<String> params = new ArrayList<String>();
		for (House house : houses) {
			sql += " update house set lng=? , lat=? where id=? ; ";	
			params.add(house.getLng().toString());
		//	System.out.println(house.getLng().toString());
		//	System.out.println(house.getLat().toString());
			params.add(house.getLat().toString());
			params.add(house.getId().toString());
		}
		Integer result = this.executeUpdate(sql, params);
		return result;
	}
}