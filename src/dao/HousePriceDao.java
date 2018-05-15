package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.House;
import bean.HouseEval;
import bean.Position;
import bean.Station;

public class HousePriceDao extends BaseDao {

	//
	public HouseEval selectHouseEval(Position pos) {
		String sql = "SELECT  * FROM house where lng=? and lat=? ;";
		HouseEval result = new HouseEval();
		List<String> params = new ArrayList<String>();
		params.add(pos.getLng());
		params.add(pos.getLat());
		ResultSet rs = this.executeQuery(sql, params);
		try {
			while (rs.next()) {
				result.setLng(rs.getString("lng"));
				result.setLat(rs.getString("lat"));
				result.setPrice(rs.getLong("aver"));
				result.setName(rs.getString("name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
		return result;
	}

	// 读出地铁数据
	public List<Position> selectSubways() {
		String sql = "SELECT DISTINCT station.lng, station.lat FROM station ;";
		List<Position> result = new ArrayList<Position>();
		ResultSet rs = this.executeQuery(sql, null);
		try {
			while (rs.next()) {
				Position po = new Position();
				po.setLng(rs.getString("lng"));
				po.setLat(rs.getString("lat"));
				// po.setCount(rs.getString("aver"));
				result.add(po);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
		return result;
	}

	// 插入地铁数据
	public Integer addSubway(List<Station> stations) {
		String sql = "";
		List<String> params = new ArrayList<String>();
		for (Station station : stations) {
			sql += " insert into station(lng,lat,name) values(?,?,?); ";
			params.add(station.getLng());
			// System.out.println(house.getLng().toString());
			// System.out.println(house.getLat().toString());
			params.add(station.getLat());
			params.add(station.getName());
		}
		Integer result = this.executeUpdate(sql, params);
		return result;
	}

	// 用于选择当前的房子
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

	// 用于得到所有的经纬度信息
	public List<Position> selectUsableHouse() {
		String sql = "SELECT DISTINCT house.lng, house.lat,house.aver FROM house WHERE house.lng > 0;";
		List<Position> result = new ArrayList<Position>();
		ResultSet rs = this.executeQuery(sql, null);
		try {
			while (rs.next()) {
				Position po = new Position();
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

	// 用于得到挑选的的经纬度信息
	public List<Position> selectSuitableHouse(int minPrice, int maxPrice) {
		String sql = "SELECT DISTINCT house.lng, house.lat,house.aver FROM house WHERE house.lng > 0 and house.aver>="
				+ String.valueOf(minPrice)
				+ " and house.aver<="
				+ String.valueOf(maxPrice) + ";";
		System.out.println(sql);
		List<Position> result = new ArrayList<Position>();
		ResultSet rs = this.executeQuery(sql, null);
		try {
			while (rs.next()) {
				Position po = new Position();
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

	// 根据地铁
	public List<Position> selectHouseBySubway(String sql) {
		List<Position> result = new ArrayList<Position>();
		ResultSet rs = this.executeQuery(sql, null);
		try {
			while (rs.next()) {
				Position po = new Position();
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

	// 用于更新房子的经纬度
	public Integer updateHouses(List<House> houses) {
		String sql = "";
		List<String> params = new ArrayList<String>();
		for (House house : houses) {
			sql += " update house set lng=? , lat=? where id=? ; ";
			params.add(house.getLng().toString());
			// System.out.println(house.getLng().toString());
			// System.out.println(house.getLat().toString());
			params.add(house.getLat().toString());
			params.add(house.getId().toString());
		}
		Integer result = this.executeUpdate(sql, params);
		return result;
	}
}