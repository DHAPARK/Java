package res;

import java.util.Arrays;
import java.util.Scanner;

class One {
   int stArr[];
   int stNum;

   public One() {;}
   void getStNum() {
      System.out.println("처리할 학생의 수를 입력하세요(최대100점):");
      this.stNum = new Scanner(System.in).nextInt();
      this.stArr = new int[stNum];
   }
   void inputStScore() {
      int cnt = 0;
      while (true) {
         if (this.stNum == cnt)
            break;
         System.out.println((cnt + 1) + "번 학생 성적:");
         this.stArr[cnt++] = new Scanner(System.in).nextInt();

      }
      System.out.println(Arrays.toString(this.stArr));
   }
}

class Two {
   One one;

   public Two(One one) {
      this.one = one;
   }

   void getAllStList() {
      System.out.println("학생 전체의 성적 리스트");
      for (int stScore : this.one.stArr) {
         System.out.println(stScore);
      }
   }
}

class Three {
   One one;
   
   public Three(One one) {
      this.one = one;
   }
   
   void getStUseStNum() {
      System.out.println("학생의 번호 입력: ");
      int stNum = new Scanner(System.in).nextInt();
      System.out.println(this.one.stArr[stNum-1]);
   }
}

class Four {
   One one;
   public Four(One one) {
      this.one = one;
   }
   
   void getMaxScoreAndMinScore() {
      int len = one.stArr.length;
      int max = one.stArr[0];
      int min = one.stArr[0];
      for (int i=0;i<len;i++) {
         if (max <= one.stArr[i]) {
            max = one.stArr[i];
         }
      }
      for (int i=0;i<len;i++) {
         if (min >= one.stArr[i]) {
            min = one.stArr[i];
         }
      }
      
      System.out.println("최고점수 : "+max);
      System.out.println("최저점수 : "+min);
   }
}

class Five {
   One one;
   public Five(One one) {
      this.one = one;
   }
   
   void getAvgAndMidScore() {
      int sum=0;
      int avg=0;
      int len = one.stArr.length;
      for(int i=0;i<len;i++) {
         sum += one.stArr[i];
      }
      avg = sum/len;
      
      int temp=0;
      
      Arrays.sort(one.stArr);
      temp  = one.stArr[len/2];
      
      System.out.println("평균 : "+avg + "중위값 : "+temp);
   }
   
}

class Six {
   One one;
   public Six(One one) {
      this.one = one;
   }
   
   void getSortedArr() {
      int len = one.stArr.length;
      Arrays.sort(this.one.stArr);
      for (int i= 0;i<len;i++) {
         System.out.println(this.one.stArr[i]);
      }
      
   }
}

public class Main {

   public static void main(String[] args) {
      One one = new One();
      Two two = new Two(one);
      Three three = new Three(one);
      Four four = new Four(one);
      Five five = new Five(one);
      Six six = new Six(one);
      System.out.println("학생 성적 처리 프로그램");
      while (true) {
         System.out.println("1) 학생 성적 입력\n"
               + "2) 학생 전체 성적 리스트 보기\n"
               + "3) 학생의 번호로 성적 보기\n"
               + "4) 성적 최고 점수, 최저 점수 보기\n"
               + "5) 성적 평균 점수, 중위값 점수 보기\n"
               + "6) 성적 정렬해서 보기\n"
               + "0)메인 메뉴로 돌아감(종료)\n");
         switch (new Scanner(System.in).nextInt()) {
         case 1: {
            one.getStNum();
            one.inputStScore();
            break;
         }
         case 2:{
            two.getAllStList();
            break;
         }
         case 3:{
            three.getStUseStNum();
            break;
         }
         case 4:{
            four.getMaxScoreAndMinScore();
            break;
         }
         case 5:{
            five.getAvgAndMidScore();
            break;
         }
         case 6:{
            six.getSortedArr();
            break;
         }
         case 0:
            System.exit(0);
         }
      }
   }

}