package algorismProject2;

import java.util.Scanner;

public class Resume2 {
	// 이진검색트리를 사용해서 검색
	// 구현 기능 (그림 참조)
	// - 상품 등록/ 상품 삭제/ 상품 검색/ 전체 상품 조회 / 종료
	// - 등록된 상품이 없는 상태에서 4 입력 시 '등록된 상품이 없습니다.' 출력

	public static void main(String[] args) {
		BinarySearchTree bt = new BinarySearchTree();
		
		int chNum;
		String pdName = "";
		int pdNum=0;
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("(1) 상품 등록 (2) 상품 삭제 (3) 상품 검색 (4) 전체 상품 조회 (5) 종료");
			switch(chNum = sc.nextInt()) {
			case 1:
				pdNum=0;
				pdName="";
				//상품 등록
				System.out.println("상품 등록");
				System.out.println("상품 번호 입력");
				pdNum = sc.nextInt();
				System.out.println("상품명 입력");
				pdName = sc.next();
				
				if(bt.Search(bt.root, pdNum) == null) {
					bt.root = bt.insert(bt.root, pdNum,pdName);
				}
				
				break;
			case 2:
				//상품 삭제
				pdNum=0;
				System.out.println("상품 삭제");
				System.out.println("상품 번호 입력 : ");
				pdNum = sc.nextInt();
				//삭제과정
				bt.delete(bt.root, pdNum);
				
				System.out.println("상품 삭제 완료");
				break;
			case 3:
				//상품 검색
				pdNum=0;
				System.out.println("상품 검색");
				System.out.println("상품 번호 입력:");
				pdNum = sc.nextInt();
				System.out.println("상품명 : ");
				
				if(bt.rSearch(bt.root, pdNum) != null) {
					String pdname = bt.rSearch(bt.root, pdNum).pdName;
					System.out.println(pdname);
				} else {
					System.out.println("탐색 실패");
				}
				
				break;
			case 4:
				
				BinarySearchTree.Node nd = bt.new Node(0, null);
				for(int i=0;i<=BinarySearchTree.cnt;i++) {
					nd = bt.Search(bt.root, i+1);
					if(nd == null) {
						break;
					}else {
						System.out.println(String.format("%d %s", nd.data,nd.pdName));
					}
				}
				//전체 상품 조회
				
				break;
			default:
				return;
			}
			
		}
	}
}
