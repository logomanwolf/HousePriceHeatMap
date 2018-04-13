package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.House;
import bean.Position;

public class HousePriceDao extends BaseDao {

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