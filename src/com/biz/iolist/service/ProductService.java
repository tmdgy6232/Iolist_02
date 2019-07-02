package com.biz.iolist.service;

import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.session.SqlSession;

import com.biz.iolist.config.DBConnection;
import com.biz.iolist.dao.ProductDao;
import com.biz.iolist.model.ProductVO;

/*
 * 상품정보의 등록, 수정, 삭제 메서드
 */
public class ProductService {

	Scanner scan = null;
	SqlSession sqlSession = null;
	ProductDao Pdao = null;

	public ProductService() {

		sqlSession = DBConnection.getSqlSessionFactory().openSession(true);
		scan = new Scanner(System.in);

		Pdao = (ProductDao) sqlSession.getMapper(ProductDao.class);

	}

	public void viewProduct() {
		System.out.println("=============================================");
		System.out.println("우리집 상품정보 리스트");
		System.out.println("---------------------------------------------");
		System.out.println("상품코드\t상품이름\t\t매입금액\t판매금액 ");
		System.out.println("---------------------------------------------");

		List<ProductVO> pList = Pdao.selectAll();

		for (ProductVO vo : pList) {
			System.out.printf("%s\t\t%s\t\t%d\t\t%d\n", vo.getP_code(), vo.getP_name(), vo.getP_iprice(),
					vo.getP_oprice());

		}
		System.out.println("----------------------------------------------");

	}

	public void insertPRO() {

		List<ProductVO> pList = Pdao.selectAll();
		while (true) {
			System.out.print("상품코드입력 (종료: -E) >> ");
			String strcode = scan.nextLine();

			int set = 0;
			for (ProductVO vo : pList) {
				if (vo.getP_code().equals(strcode))
					set += 1;
			}
			System.out.println(set);
			if (set > 0) {
				System.out.println("같은품번은 입력할 수 없습니다. 다시 입력해주세요.");
				continue;
			}
			if (strcode.equals("-E"))
				return;
			System.out.print("상품이름입력 >> ");
			String strname = scan.nextLine();
			System.out.print("매입가격입력 >> ");
			String striprice = scan.nextLine();
			int iprice = 0;
			try {

				iprice = Integer.valueOf(striprice);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("숫자만입력해주세요.");
				continue;
			}
			System.out.print("판매가격입력 >> ");
			String stroprice = scan.nextLine();
			int oprice = 0;
			try {

				oprice = Integer.valueOf(stroprice);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("숫자만입력해주세요");
				continue;
			}
			ProductVO vo = new ProductVO(strcode, strname, iprice, oprice);
			if (Pdao.insert(vo) > 0) {
				System.out.println("추가가 성공되었습니다.");
				viewProduct();
			} else
				System.out.println("추가가 실패하였습니다.");

		}

	}

	public void updatePRO() {
		while (true) {
			System.out.println("업데이트 하고자하는 상품코드 입력(종료 : -E)>>");
			String strcode = scan.nextLine();
			if (strcode.equals("-E"))
				break;
			ProductVO vo = Pdao.findByPcode(strcode);
			if(vo==null) {
				System.out.println("해당 상품은 존재하지 않습니다. 다시입력해주세요");
				continue;
			}
			System.out.println("변경할 상품코드를 입력해주세요. 없으면 enter >> ");
			strcode = scan.nextLine();
			if (strcode.isEmpty())
				strcode = vo.getP_code();
			else
				vo.setP_code(strcode);
			System.out.println("변경할 상품이름을 입력해주세요. 없으면 enter >> ");
			String strname = scan.nextLine();
			if (strname.isEmpty())
				strname = vo.getP_name();
			else
				vo.setP_name(strname);

			int iprice = 0;
			System.out.println("변경할 매입금액을 입력해주세요. 없으면 enter >> ");
			String striprice = scan.nextLine();
			try {
				if (striprice.isEmpty())
					iprice = vo.getP_iprice();
				else
					iprice = Integer.valueOf(striprice);
				vo.setP_iprice(iprice);

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("숫자만 입력해주세요.");
				continue;
			}

			int oprice = 0;
			System.out.println("변경할 판매금액을 입력해주세요. 없으면 enter >> ");
			String stroprice = scan.nextLine();
			try {
				if (stroprice.isEmpty())
					oprice = vo.getP_oprice();
				else
					oprice = Integer.valueOf(stroprice);
				vo.setP_oprice(oprice);

			} catch (Exception e) {
				// TODO: handle exception

				System.out.println("숫자만 입력해주세요.");
				continue;
			}
			if (Pdao.update(vo) > 0) {
				System.out.println("수정이 성공하였습니다.");
				viewProduct();
			} else
				System.out.println("수정이 실패하였습니다.");
		}
	}

	public void deletePRO() {

		while (true) {
			viewProduct();
			System.out.println("삭제하고자 하는 상품코드 입력 (종료 :-E)>>");
			String strcode = scan.nextLine();
			if (strcode.equals("-E"))
				break;
			if (Pdao.delete(strcode) > 0)
				System.out.println("삭제되었습니다.");
			else
				System.out.println("삭제가 되지 않았습니다.");
		}

	}

	public void mainstart() {
		viewProduct();
		while (true) {
			System.out.println("1.리스트추가\t2.수정\t3.삭제\t4.종료");
			System.out.println("수행하고자 하는 업무를 선택해주세요 >>");
			String strMenu = scan.nextLine();
			int intMenu = 0;
			try {

				intMenu = Integer.valueOf(strMenu);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("숫자만 입력해주세요");
				continue;
			}
			if (intMenu == 1)
				insertPRO();
			if (intMenu == 2)
				updatePRO();
			if (intMenu == 3)
				deletePRO();
			if (intMenu == 4)
				break;
		}
		System.out.println("종료되었습니다.");
	}
}
