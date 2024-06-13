import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Player {
	static int[] pHand = new int[5];		//プレーヤーの手札
	static int[] c1Hand = new int[5];		//CPU1の手札
	static int[] c2Hand = new int[5];		//CPI2の手札
	static int[] c3Hand = new int[5];		//CPU3の手札
	static int pChip = 20;		//プレーヤーのチップ
	static int c1Chip = 20;	//CPU1のチップ
	static int c2Chip = 20;	//CPU2のチップ
	static int c3Chip = 20;	//CPU3のチップ
	static int pBet = 0;		//プレーヤーのベット数
	static int c1Bet = 0;		//CPU1のベット数
	static int c2Bet = 0;		//CPU2のベット数
	static int c3Bet = 0;		//CPU3のベット数
	static int chipSum = pBet + c1Bet + c2Bet + c3Bet;	//ベット数の合計
	static Random rand = new Random();
	static int randum = rand.nextInt(4);
	static String[] member = {"Player","CPU1","CPU2","CPU3"};	//参加者の名前
	static String dealer = member[randum];	//ランダムに親を設定
	public void dealerChange() {	//親の変更
		if(randum<3) {
			randum++;
		}else {
			randum=0;
		}
		dealer = member[randum];
	}
}

class CardKind{
	String[] mark = {"スペード","ハート","ダイヤ","クラブ"};	//トランプのスート
	static int SUIT = 4;	//スートの種類の数
	static int RANK = 13;	//数字の種類の数
	static int[] deck = new int[SUIT*RANK];	//山札を生成
	int m;
	int n;
	public void kind(int su) {
		m = (su-1)%RANK+1;	//数字を設定
		n = (int)(su-1)/RANK;	//スートを設定
	}
	public int numberjudge(int fu) {
		kind(fu);
		return m;
	}
	public String suitjudge(int gu) {
		kind(gu);
		return mark[n] + "の";
	}
}

class Deal {
    static CardKind cardkind = new CardKind();
    //何枚目まで引いたかを数える変数
    static int dealcount=0;
    static int[] mycard = new int[5];
    //配列をListに変換する
		static int[] CP1card = new int[5];
		static int[] CP2card = new int[5];
		static int[] CP3card = new int[5];
    static List<Integer> cardlist = new ArrayList<Integer>();
    static void deal() {
      for (int k=0;k<CardKind.deck.length;k++) {
        cardlist.add(k+1);
      }
      //Listの要素の順番をシャッフルする
      Collections.shuffle(cardlist);
      //System.out.println(cardlist);
      //手札を引く
      for(int i=0; i<mycard.length; i++) {
      mycard[i]=cardlist.get(dealcount);
      dealcount++; //山札を減らす
			CP1card[i]=cardlist.get(dealcount);
			dealcount++;
			CP2card[i]=cardlist.get(dealcount);
			dealcount++;
			CP3card[i]=cardlist.get(dealcount);
			dealcount++;
      }
      //System.out.println(dealcount+"枚引いた");
      System.out.println("自分のカードは");
      //自分の手札を表示する
      for(int j=0;j<mycard.length;j++) {
        System.out.print((j+1)+"枚目:"+cardkind.suitjudge(mycard[j]) + cardkind.numberjudge(mycard[j])+" ");
      }
      System.out.println("");
      }
}


class Chip1{
    int input;	//入力された値
    static int drop = 0;
    boolean dropCheck = true;
    boolean betCheck = true;
    BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
    Chip1(){
    	System.out.println("ドロップしますか？");
    	while(dropCheck) {
    		System.out.println("ドロップする場合は1,ドロップしない場合は0を入力してください。");
    		try {
    			drop = Integer.parseInt(buf.readLine());
    			if(drop == 1) {//１が入力された場合はゲームを降りる
    				System.out.println("ドロップしました。");
    				dropCheck = false;
    			}else if(drop == 0){//0が入力された場合はゲームを続ける
    				setDeal();
    				dropCheck = false;
    			}else {
    				System.out.println("決められた値を入力してください");//1または0が入力されなかった場合
    			}
    		}catch(Exception e) {
    			System.out.println("決められた値を入力してください");
    		}
    	}
    }
    public void setDeal(){//チップを掛ける
    	while(betCheck) {
    		try {
    			System.out.println("チップを"+(Player.c3Bet - Player.pBet)+"枚以上賭けてください。");
    			input = Integer.parseInt(buf.readLine());//掛けるチップ数を入力
    			if(Player.pBet + input == Player.c3Bet) {
    				Player.pBet = Player.pBet + input;
    				Player.pChip = Player.pChip - input;
    				betCheck = false;
    			}else if(Player.pBet + input > Player.c3Bet) {
    				if(Player.pBet + input> 4) {
    					System.out.println("賭けているチップが上限を超えています。");
    				}else {
    					Player.pBet = Player.pBet + input;
    					Player.pChip = Player.pChip - input;
    					betCheck = false;
    				}
    			}else {
    				System.out.println((Player.c3Bet - Player.pBet)+"枚以上賭けてください。");
    			}
    		}catch(Exception e) {
    			System.out.println("決められた値を入力してください");
    		}
    	}
    }
}

class CPUBet extends HandJudge {
	static int dealerNext1 = Player.randum + 1;
	static int dealerNext2 = dealerNext1 % 4;	//誰からチップを賭けるか
	static int betTime = 0;
	static int pBet2 = 1;		//Playerが前回何枚チップを賭けていたか
	static int c1Bet2 = 1;		//CPU1が前回何枚チップを賭けていたか
	static int c2Bet2 = 1;		//CPU2が前回何枚チップを賭けていたか
	static int c3Bet2 = 1;		//CPU3が前回何枚チップを賭けていたか
	static int callTime = 0;	//コール、パス、チェックをした回数
	static int pCheck = 0;
	static int c1Check = 0;	//CPU1がチェックできるかどうか
	static int c2Check = 0;	//CPU2がチェックできるかどうか
	static int c3Check = 0;	//CPU3がチェックできるかどうか
	static int nextBet = 0;	//誰が最初にビットしたか
	static int time = 0;	//誰かがビットしたかどうか
	public void chipBet() {	//CPUのチップの動きを表示
		if(Player.pBet != 1 || Player.c1Bet != 1 || Player.c2Bet != 1 || Player.c3Bet !=1) {
			betTime++;
		}
		if(betTime==0) {
			CPUBetJudge(dealerNext2, HandJudgeMethod(Player.pHand), HandJudgeMethod(Player.c1Hand), HandJudgeMethod(Player.c2Hand), HandJudgeMethod(Player.c3Hand));
			if(dealerNext2 == 1) {
				if(Player.pBet == 1 && Player.c1Bet == 1 && Player.c2Bet == 1 && Player.c3Bet ==1) {
					System.out.println(Player.member[dealerNext2] + "がパスしました。");
					callTime++;
				}else {
					System.out.println(Player.member[dealerNext2] + "がビッド " + (Player.c1Bet - Player.pBet) + "枚追加しました。");
					callTime = 1;
					pCheck++;
					c1Check++;
					c2Check++;
					c3Check++;
					nextBet = 1;
					time++;
				}
				c1Bet2 = Player.c1Bet;
			}else if(dealerNext2 == 2) {
				if(Player.pBet == 1 && Player.c1Bet == 1 && Player.c2Bet == 1 && Player.c3Bet ==1) {
					System.out.println(Player.member[dealerNext2] + "がパスしました。");
					callTime++;
				}else {
					System.out.println(Player.member[dealerNext2] + "がビッド　" + (Player.c2Bet - Player.c1Bet) + "枚追加しました。");
					callTime = 1;
					pCheck++;
					c1Check++;
					c2Check++;
					c3Check++;
					nextBet = 1;
					time++;
				}
				c2Bet2 = Player.c2Bet;
			}else if(dealerNext2 == 3) {
				if(Player.pBet == 1 && Player.c1Bet == 1 && Player.c2Bet == 1 && Player.c3Bet ==1) {
					System.out.println(Player.member[dealerNext2] + "がパスしました。");
					callTime++;
				}else {
					System.out.println(Player.member[dealerNext2] + "がビッド　" + (Player.c3Bet - Player.c2Bet) + "枚追加しました。");
					callTime = 1;
					pCheck++;
					c1Check++;
					c2Check++;
					c3Check++;
					nextBet = 1;
					time++;
				}
				c3Bet2 = Player.c3Bet;
			}else {
				if(Player.pBet == 1 && Player.c1Bet == 1 && Player.c2Bet == 1 && Player.c3Bet ==1) {
					System.out.println(Player.member[dealerNext2] + "がパスしました。");
					callTime++;
				}else {
					System.out.println(Player.member[dealerNext2] + "がビット　" + (Player.c2Bet - Player.c1Bet) + "枚追加しました。");
					callTime = 1;
					pCheck++;
					c1Check++;
					c2Check++;
					c3Check++;
					time++;
				}
				pBet2 = Player.pBet;
			}
		}else {
			CPUBetJudge(dealerNext2, HandJudgeMethod(Player.pHand), HandJudgeMethod(Player.c1Hand), HandJudgeMethod(Player.c2Hand), HandJudgeMethod(Player.c3Hand));
			if(dealerNext2 == 1) {
				if(Chip1.drop == 0) {
					if(Player.c1Bet == Player.pBet) {
						if(Player.c1Bet == c1Bet2  && c1Check == 0) {
							System.out.println(Player.member[dealerNext2] + "がチェックしました。");
							callTime++;
						}else {
							System.out.println(Player.member[dealerNext2] + "がコールしました。");
							callTime++;
						}
					}else if(Player.pBet < Player.c1Bet) {
						System.out.println(Player.member[dealerNext2] + "がレイズ　" + (Player.c1Bet - c1Bet2) + "枚ベットしました。");
						callTime = 1;
						pCheck++;
						c1Check++;
						c2Check++;
						c3Check++;
						nextBet = 1;
					}
					c1Bet2 = Player.c1Bet;
					c1Check++;
				}else {
					if(Player.c1Bet == Player.c3Bet) {
						if(Player.c1Bet == c1Bet2  && c1Check == 0) {
							System.out.println(Player.member[dealerNext2] + "がチェックしました。");
							callTime++;
						}else {
							System.out.println(Player.member[dealerNext2] + "がコールしました。");
							callTime++;
						}
					}else if(Player.c3Bet < Player.c1Bet) {
						System.out.println(Player.member[dealerNext2] + "がレイズ　" + (Player.c1Bet - c1Bet2) + "枚ベットしました。");
						callTime = 1;
						pCheck++;
						c1Check++;
						c2Check++;
						c3Check++;
						nextBet = 1;
					}
					c1Bet2 = Player.c1Bet;
					c1Check++;
				}
			}else if(dealerNext2 == 2) {
				if(Player.c1Bet == Player.c2Bet) {
					if(Player.c2Bet == Player.c1Bet && c2Check == 0) {
						System.out.println(Player.member[dealerNext2] + "がチェックしました。");
						callTime++;
					}else {
						System.out.println(Player.member[dealerNext2] + "がコールしました。");
						callTime++;
					}
				}else if(Player.c1Bet < Player.c2Bet) {
					System.out.println(Player.member[dealerNext2] + "がレイズ　" + (Player.c2Bet - c2Bet2) + "枚ベットしました。");
					callTime = 1;
					pCheck++;
					c1Check++;
					c2Check++;
					c3Check++;
					nextBet = 1;
				}
				c2Bet2 = Player.c2Bet;
				c2Check++;
			}else if(dealerNext2 == 3) {
				if(Player.c2Bet == Player.c3Bet) {
					if(Player.c3Bet == Player.c2Bet && c3Check == 0) {
						System.out.println(Player.member[dealerNext2] + "がチェックしました。");
						callTime++;
					}else {
						System.out.println(Player.member[dealerNext2] + "がコールしました。");
						callTime++;
					}
				}else if(Player.c2Bet < Player.c3Bet) {
					System.out.println(Player.member[dealerNext2] + "がレイズ　" + (Player.c3Bet - c3Bet2) + "枚ベットしました。");
					callTime = 1;
					pCheck++;
					c1Check++;
					c2Check++;
					c3Check++;
				}
				c3Bet2 = Player.c3Bet;
				c3Check++;
			}else {
				if(Player.pBet == Player.c3Bet) {
					if(Player.pBet == Player.c3Bet && pCheck == 0) {
						System.out.println(Player.member[dealerNext2] + "がチェックしました。");
						callTime++;
					}else {
						System.out.println(Player.member[dealerNext2] + "がコールしました。");
						callTime++;
					}
				}else {
					System.out.println(Player.member[dealerNext2] + "がレイズ　" + (Player.pBet - pBet2) + "枚ベットしました。");
					callTime = 1;
					pCheck++;
					c1Check++;
					c2Check++;
					c3Check++;
				}
				pBet2 = Player.pBet;
				pCheck++;
			}
		}
		dealerNext1++;
		dealerNext2 = dealerNext1 % 4;
	}
	public void CPUBetJudge(int CPUNumber, double pPoint,double c1Point, double c2Point, double c3Point) {	//チップを賭けるか判断
		Random rand = new Random();
		int randum = rand.nextInt(100) + 1;
		if(betTime == 0) {	//ビッドするかどうか
			if(CPUNumber == 1) {	//CPU1
				if(c1Point > 8 && randum <= 85) {
					Player.c1Bet++;
					Player.c1Chip--;
				}else if(c1Point > 6 && randum <= 60) {
					Player.c1Bet++;
					Player.c1Chip--;
				}else if(c1Point > 3 && randum <= 40) {
					Player.c1Bet++;
					Player.c1Chip--;
				}else if(c1Point > 2 && randum <= 20) {
					Player.c1Bet++;
					Player.c1Chip--;
				}else if(randum <= 5) {
					Player.c1Bet++;
					Player.c1Chip--;
				}
			}else if(CPUNumber == 2){	//CPU2
				if(c2Point > 8 && randum <= 85) {
					Player.c2Bet++;
					Player.c2Chip--;
				}else if(c2Point > 6 && randum <= 60) {
					Player.c2Bet++;
					Player.c2Chip--;
				}else if(c2Point > 3 && randum <= 40) {
					Player.c2Bet++;
					Player.c2Chip--;
				}else if(c2Point > 2 && randum <= 20) {
					Player.c2Bet++;
					Player.c2Chip--;
				}else if(randum <= 5) {
					Player.c2Bet++;
					Player.c2Chip--;
				}
			}else if(CPUNumber == 3) {	//CPU3
				if(c3Point > 8 && randum <= 85) {
					Player.c3Bet++;
					Player.c3Chip--;
				}else if(c3Point > 6 && randum <= 60) {
					Player.c3Bet++;
					Player.c3Chip--;
				}else if(c3Point > 3 && randum <= 40) {
					Player.c3Bet++;
					Player.c3Chip--;
				}else if(c3Point > 2 && randum <= 20) {
					Player.c3Bet++;
					Player.c3Chip--;
				}else if(randum <= 5) {
					Player.c3Bet++;
					Player.c3Chip--;
				}
			}
		}else {		//レイズするかどうか
			if(CPUNumber == 1) {	//CPU1
				if(Chip1.drop == 0) {	//Playerがドロップしていない場合
					if(Player.pBet == 4) {
						Player.c1Chip = Player.c1Chip - (Player.pBet - Player.c1Bet);
						Player.c1Bet = Player.pBet;
					}else {
						if(c1Point > 8 && randum <= 85) {
							Player.c1Chip = Player.c1Chip - (Player.pBet - Player.c1Bet +1);
							Player.c1Bet = Player.c1Bet + (Player.pBet - Player.c1Bet +1);
						}else if(c1Point > 6 && randum <= 60) {
							Player.c1Chip = Player.c1Chip - (Player.pBet - Player.c1Bet +1);
							Player.c1Bet = Player.c1Bet + (Player.pBet - Player.c1Bet +1);
						}else if(c1Point > 4 && randum <= 40) {
							Player.c1Chip = Player.c1Chip - (Player.pBet - Player.c1Bet +1);
							Player.c1Bet = Player.c1Bet + (Player.pBet - Player.c1Bet +1);
						}else if(c1Point > 3 && randum <= 25) {
							Player.c1Chip = Player.c1Chip - (Player.pBet - Player.c1Bet +1);
							Player.c1Bet = Player.c1Bet + (Player.pBet - Player.c1Bet +1);
						}else if(randum <= 5) {
							Player.c1Chip = Player.c1Chip - (Player.pBet - Player.c1Bet +1);
							Player.c1Bet = Player.c1Bet + (Player.pBet - Player.c1Bet +1);
						}else {
							Player.c1Chip = Player.c1Chip - (Player.pBet - Player.c1Bet);
							Player.c1Bet = Player.c1Bet + (Player.pBet - Player.c1Bet);
						}
					}
				}else {	//Playerがドロップしている場合
					if(Player.c3Bet == 4) {
						Player.c1Chip = Player.c1Chip - (Player.c3Bet - Player.c1Bet);
						Player.c1Bet = Player.c3Bet;
					}else {
						if(c1Point > 8 && randum <= 85) {
							Player.c1Chip = Player.c1Chip - (Player.c3Bet - Player.c1Bet +1);
							Player.c1Bet = Player.c1Bet + (Player.c3Bet - Player.c1Bet +1);
						}else if(c1Point > 6 && randum <= 60) {
							Player.c1Chip = Player.c1Chip - (Player.c3Bet - Player.c1Bet +1);
							Player.c1Bet = Player.c1Bet + (Player.c3Bet - Player.c1Bet +1);
						}else if(c1Point > 4 && randum <= 40) {
							Player.c1Chip = Player.c1Chip - (Player.c3Bet - Player.c1Bet +1);
							Player.c1Bet = Player.c1Bet + (Player.c3Bet - Player.c1Bet +1);
						}else if(c1Point > 3 && randum <= 25) {
							Player.c1Chip = Player.c1Chip - (Player.c3Bet - Player.c1Bet +1);
							Player.c1Bet = Player.c1Bet + (Player.c3Bet - Player.c1Bet +1);
						}else if(randum <= 5) {
							Player.c1Chip = Player.c1Chip - (Player.c3Bet - Player.c1Bet +1);
							Player.c1Bet = Player.c1Bet + (Player.c3Bet - Player.c1Bet +1);
						}else {
							Player.c1Chip = Player.c1Chip - (Player.c3Bet - Player.c1Bet);
							Player.c1Bet = Player.c1Bet + (Player.c3Bet - Player.c1Bet);
						}
					}
				}
			}else if(CPUNumber == 2){	//CPU2
				if(Player.c1Bet == 4) {
					Player.c2Chip = Player.c2Chip - (Player.c1Bet - Player.c2Bet);
					Player.c2Bet = Player.c1Bet;
				}else {
					if(c2Point > 8 && randum <= 85) {
						Player.c2Chip = Player.c2Chip - (Player.c1Bet - Player.c2Bet +1);
						Player.c2Bet = Player.c2Bet + (Player.c1Bet - Player.c2Bet +1);
					}else if(c2Point > 6 && randum <= 60) {
						Player.c2Chip = Player.c2Chip - (Player.c1Bet - Player.c2Bet +1);
						Player.c2Bet = Player.c2Bet + (Player.c1Bet - Player.c2Bet +1);
					}else if(c2Point > 4 && randum <= 40) {
						Player.c2Chip = Player.c2Chip - (Player.c1Bet - Player.c2Bet +1);
						Player.c2Bet = Player.c2Bet + (Player.c1Bet - Player.c2Bet +1);
					}else if(c2Point > 3 && randum <= 25) {
						Player.c2Chip = Player.c2Chip - (Player.c1Bet - Player.c2Bet +1);
						Player.c2Bet = Player.c2Bet + (Player.c1Bet - Player.c2Bet +1);
					}else if(randum <= 5) {
						Player.c2Chip = Player.c2Chip - (Player.c1Bet - Player.c2Bet +1);
						Player.c2Bet = Player.c2Bet + (Player.c1Bet - Player.c2Bet +1);
					}else {
						Player.c2Chip = Player.c2Chip - (Player.c1Bet - Player.c2Bet);
						Player.c2Bet = Player.c2Bet + (Player.c1Bet - Player.c2Bet);
					}
				}
			}else if(CPUNumber == 3) {	//CPU3
				if(Player.c2Bet == 4) {
					Player.c3Chip = Player.c3Chip - (Player.c2Bet - Player.c3Bet);
					Player.c3Bet = Player.c2Bet;
				}else {
					if(c3Point > 8 && randum <= 85) {
						Player.c3Chip = Player.c3Chip - (Player.c2Bet - Player.c3Bet +1);
						Player.c3Bet = Player.c3Bet + (Player.c2Bet - Player.c3Bet +1);
					}else if(c3Point > 6 && randum <= 60) {
						Player.c3Chip = Player.c3Chip - (Player.c2Bet - Player.c3Bet +1);
						Player.c3Bet = Player.c3Bet + (Player.c2Bet - Player.c3Bet +1);
					}else if(c3Point > 4 && randum <= 40) {
						Player.c3Chip = Player.c3Chip - (Player.c2Bet - Player.c3Bet +1);
						Player.c3Bet = Player.c3Bet + (Player.c2Bet - Player.c3Bet +1);
					}else if(c3Point > 3 && randum <= 25) {
						Player.c3Chip = Player.c3Chip - (Player.c2Bet - Player.c3Bet +1);
						Player.c3Bet = Player.c3Bet + (Player.c2Bet - Player.c3Bet +1);
					}else if(randum <= 5) {
						Player.c3Chip = Player.c3Chip - (Player.c2Bet - Player.c3Bet +1);
						Player.c3Bet = Player.c3Bet + (Player.c2Bet - Player.c3Bet +1);
					}else {
						Player.c3Chip = Player.c3Chip - (Player.c2Bet - Player.c3Bet);
						Player.c3Bet = Player.c3Bet + (Player.c2Bet - Player.c3Bet);
					}
				}
			}
		}
	}
	public void starterChange() {		//ビットした人がいれば順番を変更
		if(time != 0) {
			dealerNext1 = nextBet;
			dealerNext2 = dealerNext1 % 4;
		}
		pCheck = 0;
		c1Check = 0;
		c2Check = 0;
		c3Check = 0;
		betTime++;
		callTime = 0;
	}
	public void reset() {		//値をリセット
		dealerNext1 = Player.randum + 1;
		dealerNext2 = CPUBet.dealerNext1 % 4;
		pCheck = 0;
		c1Check = 0;
		c2Check = 0;
		c3Check = 0;
		betTime = 0;
		callTime = 0;
		time = 0;
		pBet2 = 1;
		c1Bet2 = 1;
		c2Bet2 = 1;
		c3Bet2 = 1;
	}
}

class Exchange {
	public void exchange() throws IOException{
	    BufferedReader change = new BufferedReader(new InputStreamReader(System.in));
	    Scanner stdIn = new Scanner(System.in);
	    CardKind cardkind2 = new CardKind();
	    Deal deal1=new Deal();
	    CPUExchange ce1 = new CPUExchange();
	    CPUExchange ce2 = new CPUExchange();
	    CPUExchange ce3 = new CPUExchange();
	    ce1.CPUJudge(Player.c1Hand);
	    ce2.CPUJudge(Player.c2Hand);
	    ce3.CPUJudge(Player.c3Hand);
	    int CardExchange;
	    int CardChangeTurnCount=0;
	    boolean numberjudge1=true;
	    boolean judge1=true;
	    boolean judge2=true;
	    boolean judge3=true;
	    boolean judge4=true;
	    boolean judge5=true;
	    int CardExchangeNumber1;
	    int CardExchangeNumber2;
	    int CardExchangeNumber3;
	    int CardExchangeNumber4;
	    int CardExchangeNumber5;
	    int notchangecount=0;
	    System.out.println();
	      int changecontinueflag=0;
	      if(Chip1.drop == 0) {
	    	  System.out.println("手札を交換する場合は1、交換しない場合は0を入力してください。");
		       while(numberjudge1){
		          String s;
		          s=change.readLine();
		          try {
		            CardExchange = Integer.parseInt(s);
		         }catch(Exception e) {
		           System.out.println("決められた値を入力してください");
		           continue;
		         }
		         if(CardExchange==1) {
		          numberjudge1 = false;
		          changecontinueflag = 1;
		        }else if(CardExchange==0){
		          numberjudge1 = false;
		          changecontinueflag = 0;
		         }else {
		           System.out.println("0か1を入力してください。");
		         }
		       }

		     if (changecontinueflag==1) {
		      System.out.println("どのカードを交換しますか？");//交換する手札を表示する
		      for(int i=0;i<deal1.mycard.length;i++) {
		        System.out.print("("+i+"):"+cardkind2.suitjudge(deal1.mycard[i]) + cardkind2.numberjudge(deal1.mycard[i])+" ");
		      }
		      System.out.println();
		      System.out.println("交換する場合は1、しない場合は0を入力してください");


		        while(judge1){
		          System.out.print("(0)"+cardkind2.suitjudge(deal1.mycard[0])+cardkind2.numberjudge(deal1.mycard[0])+":");
		           String s1;
		           s1=change.readLine();
		           try {
		             CardExchangeNumber1 = Integer.parseInt(s1);
		          }catch(Exception e) {
		            System.out.println("決められた値を入力してください");
		            continue;
		          }
		          if(CardExchangeNumber1==1) {
		           judge1 = false;
		           deal1.mycard[0] = deal1.cardlist.get(deal1.dealcount);
		           deal1.dealcount++;
		           //山札を減らす
		         }else if(CardExchangeNumber1==0){
		           judge1 = false;
		           notchangecount++;
		          }else {
		            System.out.println("1か0を入力してください。");
		          }
		        }
		        System.out.println();


		        while(judge2){
		          System.out.print("(1)"+cardkind2.suitjudge(deal1.mycard[1])+cardkind2.numberjudge(deal1.mycard[1])+":");
		           String s2;
		           s2=change.readLine();
		           try {
		             CardExchangeNumber2 = Integer.parseInt(s2);
		          }catch(Exception e) {
		            System.out.println("1か0のどちらかの値でを入力してください。");
		            continue;
		          }
		          if(CardExchangeNumber2==1) {
		           judge2 = false;
		           deal1.mycard[1] = deal1.cardlist.get(deal1.dealcount);
		           deal1.dealcount++;
		           //山札を減らす
		         }else if(CardExchangeNumber2==0){
		           judge2 = false;
		           notchangecount++;
		          }else {
		            System.out.println("1か0のどちらかの値でを入力してください。");
		          }
		        }
		        System.out.println();


		        while(judge3){
		          System.out.print("(2)"+cardkind2.suitjudge(deal1.mycard[2])+cardkind2.numberjudge(deal1.mycard[2])+":");
		           String s3;
		           s3=change.readLine();
		           try {
		             CardExchangeNumber3 = Integer.parseInt(s3);
		          }catch(Exception e) {
		            System.out.println("決められた値を入力してください");
		            continue;
		          }
		          if(CardExchangeNumber3==1) {
		           judge3 = false;
		           deal1.mycard[2] = deal1.cardlist.get(deal1.dealcount);
		           deal1.dealcount++;
		           //山札を減らす
		         }else if(CardExchangeNumber3==0){
		           judge3 = false;
		           notchangecount++;
		          }else {
		            System.out.println("1か0のどちらかの値でを入力してください。");
		          }
		        }
		        System.out.println();


		        while(judge4){
		          System.out.print("(3):"+cardkind2.suitjudge(deal1.mycard[3])+cardkind2.numberjudge(deal1.mycard[3])+":");
		           String s4;
		           s4=change.readLine();
		           try {
		             CardExchangeNumber4 = Integer.parseInt(s4);
		          }catch(Exception e) {
		            System.out.println("1か0のどちらかの値でを入力してください。");
		            continue;
		          }
		          if(CardExchangeNumber4==1) {
		           judge4 = false;
		           deal1.mycard[3] = deal1.cardlist.get(deal1.dealcount);
		           deal1.dealcount++;
		           //山札を減らす
		         }else if(CardExchangeNumber4==0){
		           judge4 = false;
		           notchangecount++;
		          }else {
		            System.out.println("1か0のどちらかの値でを入力してください。");
		          }
		        }
		        System.out.println();


		        while(judge5){
		          System.out.print("(4)"+cardkind2.suitjudge(deal1.mycard[4])+cardkind2.numberjudge(deal1.mycard[4])+":");
		           String s5;
		           s5=change.readLine();
		           try {
		             CardExchangeNumber5 = Integer.parseInt(s5);
		          }catch(Exception e) {
		            System.out.println("1か0のどちらかの値でを入力してください。");
		            continue;
		          }
		          if(CardExchangeNumber5==1) {
		           judge5 = false;
		           deal1.mycard[4] = deal1.cardlist.get(deal1.dealcount);
		           deal1.dealcount++;
		           //山札を減らす
		         }else if(CardExchangeNumber5==0){
		           judge5 = false;
		           notchangecount++;
		          }else {
		            System.out.println("1か0のどちらかの値でを入力してください。");
		          }
		        }
		        System.out.println();
		    }
		    if(changecontinueflag==0 || notchangecount==5) {
		      System.out.println("手札はそのままです。");
		    } else {
		      System.out.println("手札が入れ替わりました");
		    }
		      for(int j=0;j<deal1.mycard.length;j++) {
		        System.out.print("("+j+"):"+cardkind2.suitjudge(deal1.mycard[j]) + cardkind2.numberjudge(deal1.mycard[j])+" "); //自分の手札を表示する
		    }
	      }
	    for(int l=0;l<ce1.CardsExchanger.length; l++) {
	    	if(ce1.CardsExchanger[l]==2) {
	      		Player.c1Hand[l]=deal1.cardlist.get(deal1.dealcount);
	      		deal1.dealcount++;
	      	}
	      	if(ce2.CardsExchanger[l]==2) {
	      		Player.c2Hand[l]=deal1.cardlist.get(deal1.dealcount);
	      		deal1.dealcount++;
	      	}
	      	if(ce3.CardsExchanger[l]==2) {
	      		Player.c3Hand[l]=deal1.cardlist.get(deal1.dealcount);
	      		deal1.dealcount++;
	      	}
	    }
	}
}



class CPUExchange extends HandJudge {
	int [] CardsExchanger = {1, 1, 1, 1, 1};
	Random random = new Random();
	int randomValue;
	int notcontinuation;
	int max;
	public int[] CPUJudge(int [] hand) {
		for(int i = 0; i < number.length; i++) {
			number[i] = 0;
		}
		for ( int i = 0; i < suit.length; i++ ) {
			suit[i] = 0;
		}
		for(int i = 0; i < hand.length; i++) {
			num = (hand[i] - 1)%13;				//数
			mark  = (int)(hand[i]-1)/13;   			//マーク
			++suit[mark];   					//同じマークをカウント
			++number[num]; 						//同じ数字をカウント
		}
		number[13] = number[0];
		//同じ数字の最大個数を取得
		for ( int i = 0; i < number.length - 1; i++ ) {
			if ( number_max < number[i] ) {
				number_max = number[i];
			}
		}
		//同じマークの最大個数を取得
		for ( int i = 0; i < suit.length; i++ ) {
			if ( suit_max < suit[i] ) {
				suit_max = suit[i];
			}
		}
		//ペアの個数を取得
		for(int i = 1; i < number.length; i++) {
			if(2 == number[i]) {
				pair_num++;
			}
		}
		//役による点数を取得
		switch ((int)HandJudgeMethod(hand)) {
		// n番目のカードを交換するかを1(交換しない)2(交換する）で表現しています
			case 1:
				notcontinuation = 1;
				if(suit_max == 4) {
					for(int i = 0; i < suit.length; i++) {
						if (suit[i] == 1) {
							for(int j = 0; j < hand.length; j ++) {
								if((int)hand[j]/13 == i) {
									randomValue = random.nextInt(100);
									if(randomValue > 3) {
										CardsExchanger[j] = 2;
									}
									return CardsExchanger;
								}
							}
						}
					}
				}
				continuation = 0; //初期化
				firstnum = 0;
				for(int i = 0; i < 14; ++i) {
					if( 1 != number[i]) {
						continuation = 0;
						firstnum = 0;
					}
					else {
						++continuation;
						if(1 == continuation) {
							firstnum = i + 1;
						}
						if(4 == continuation) {
							for ( int j = 0; j < number.length - 1; j++ ) {
								if(1 == number[j]) {
									if(firstnum == 11) {
										for(int k = 0; k < hand.length; k++) {
											if(numberjudge(hand[k]) < firstnum || numberjudge(hand[k]) > firstnum + 3) {
												if(number[numberjudge(hand[k]) - 1] == 1 && numberjudge(hand[k]) != 1) {
													CardsExchanger[k] = 2;
													return CardsExchanger;
												}
											}
										}
									}
									for(int k = 0; k < hand.length; k++) {
										if(numberjudge(hand[k]) < firstnum || numberjudge(hand[k]) > firstnum + 3) {
											if(number[numberjudge(hand[k]) - 1] == 1) {
												CardsExchanger[k] = 2;
												return CardsExchanger;
											}
										}
									}
								}
							}
						}
					}
				}
				for(int j = 0; j < hand.length; j++) {
					if(max < numberjudge(hand[j])){
						max = numberjudge(hand[j]);
					}
				}
				for(int j = 0; j < hand.length; j++) {
					if(numberjudge(hand[j]) == 1) {
						max = 1;
					}
				}
				for(int j = 0; j < hand.length; j++) {
					if(max != numberjudge(hand[j])) {
						randomValue = random.nextInt(100);
						if(randomValue > 4) {
							CardsExchanger[j] = 2;
						}
					}
				}
				return CardsExchanger;
			case 2:
				for ( int i = 0; i < number.length - 1; i++ ) {
					if(1 == number[i]) {
						for(int j =0; j < hand.length; j++) {
							if(numberjudge(hand[j]) == i + 1) {
								CardsExchanger[j] = 2;
							}
						}
					}
				}
				return CardsExchanger;
			case 3:
				for ( int i = 0; i < number.length - 1; i++ ) {
					if(1 == number[i]) {
						for(int j =0; j < hand.length; j++) {
							if(numberjudge(hand[j]) == i + 1) {
								CardsExchanger[j] = 2;
							}
						}
					}
				}
				return CardsExchanger;
			case 4:
				for ( int i = 0; i < number.length - 1; i++ ) {
					if(1 == number[i]) {
						for(int j =0; j < hand.length; j++) {
							if(numberjudge(hand[j]) == i + 1) {
								CardsExchanger[j] = 2;
							}
						}
					}
				}
				return CardsExchanger;
			case 5:
				return CardsExchanger;
			case 6:
				return CardsExchanger;
			case 7:
				return CardsExchanger;
			case 8:
				for ( int i = 0; i < number.length - 1; i++ ) {
					if(1 == number[i]) {
						for(int j =0; j < hand.length; j++) {
							if(numberjudge(hand[j]) == i + 1) {
								CardsExchanger[j] = 2;
							}
						}
					}
				}
				return CardsExchanger;
			case 9:
				return CardsExchanger;
			case 10:
				return CardsExchanger;
		}
		return CardsExchanger;
	}
}

class HandJudge extends CardKind {
	double result = 0;							//メソッドの結果を入れる
	static int[] number = new int[14];			//同じ数字をカウント
	static int[] suit = new int[4];				//同じマークをカウント
	static int num = 0;								//数
	static int mark = 0;								//マーク
	static int number_max = 0;							//同じ数字の最大個数
	static int suit_max = 0;							//同じマークの最大個数
	static int pair_num = 0;							//ペアの数
	static int drawcase1 = 0;							//役が同じ時に数字の大きさを考慮するための一時的なint
	static int drawcase2 = 0;
	static int drawcase3 = 0;
	static int continuation = 0;						//ストレートを判定するための一時的なint
	static int firstnum = 0;
	static boolean loyal_straight_flush = false;	//ロイヤルストレートフラッシュの判定
	static boolean straight_flush = false;		//ストレートフラッシュの判定
	static boolean four_card = false;			//フォーカードの判定
	static boolean full_house = false;			//フルハウスの判定
	static boolean flush = false;				//フラッシュの判定
	static boolean straight = false; 			//ストレートの判定
	static boolean three_card = false;			//スリーカードの判定
	static boolean two_pair = false;				//ツーペアの判定
	static boolean one_pair = false;				//ワンペアの判定
	static boolean pig = false;					//役なしの判定
	public HandJudge() {
		System.out.print("");
	}
	public HandJudge(int [] hands, String member) {
		result = HandJudgeMethod(hands);
		System.out.print(member + "の手札は");
		for(int i =0; i < hands.length; i++) {
			System.out.print(suitjudge(hands[i]) + numberjudge(hands[i]) + " ");
		}
		System.out.print("で");
		switch ((int)HandJudgeMethod(hands)) {
		case 1:
			System.out.println("役なしです");
			break;
		case 2:
			System.out.println("ワンペアです");
			break;
		case 3:
			System.out.println("ツーペアです");
			break;
		case 4:
			System.out.println("スリーカードです");
			break;
		case 5:
			System.out.println("ストレートです");
			break;
		case 6:
			System.out.println("フラッシュです");
			break;
		case 7:
			System.out.println("フルハウスです");
			break;
		case 8:
			System.out.println("フォーカードです");
			break;
		case 9:
			System.out.println("ストレートフラッシュです");
			break;
		case 10:
			System.out.println("ロイヤルストレートフラッシュです");
			break;
		}
	}
	public static double HandJudgeMethod(int[] hand) { //数字の大小で役の強さを決めています
		number = new int[14]; //初期化
		suit = new int[4];
		num = 0;
		mark = 0;
		number_max = 0;
		suit_max = 0;
		pair_num = 0;
		drawcase1 = 0;
		drawcase2 = 0;
		drawcase3 = 0;
		continuation = 0;
		firstnum = 0;
		loyal_straight_flush = false;
		straight_flush = false;
		four_card = false;
		full_house = false;
		flush = false;
		straight = false;
		three_card = false;
		two_pair = false;
		one_pair = false;
		pig = false;
		for(int i = 0; i < number.length; i++) {
			number[i] = 0;
		}
		for ( int i = 0; i < suit.length; i++ ) {
			suit[i] = 0;
		}
		for(int i = 0; i < hand.length; i++) {
			num = (hand[i] - 1)%13;				//数
			mark  = (int)(hand[i]-1)/13;   			//マーク
			++suit[mark];   					//同じマークをカウント
			++number[num]; 						//同じ数字をカウント
		}
		number[13] = number[0];
		//同じ数字の最大個数を取得
		for ( int i = 0; i < number.length - 1; i++ ) {
			if ( number_max < number[i] ) {
				number_max = number[i];
			}
		}
		//同じマークの最大個数を取得
		for ( int i = 0; i < suit.length; i++ ) {
			if ( suit_max < suit[i] ) {
				suit_max = suit[i];
			}
		}
		//ペアの個数を取得
		for(int i = 1; i < number.length; i++) {
			if(2 == number[i]) {
				pair_num++;
			}
		}
		//フォーカードを判定
		if ( 4 == number_max ) {
			for ( int i = 1; i < number.length; i++ ) {
				if(4 == number[i]) {
					drawcase1 = i + 1;
				}
				if(1 == number[i]) {
					drawcase2 = i + 1;
				}
			}
			four_card =true;
			return 8 + 0.01 * drawcase1 + 0.0001 * drawcase2;
		}
		//ストレート以上かどうか判定
		for(int i = 0; i < 14; ++i) {
			if( 1 != number[i]) {
				continuation = 0;
				firstnum = 0;
			}
			else {
				++continuation;
				if(1 == continuation) {
					firstnum = i + 1;
				}
				if(5 == continuation) {
					straight = true;
					break;
				}
			}
		}
		//ロイヤルストレートフラッシュを判定
		if ( ( 5 == suit_max ) && straight ) {
			if(10 == firstnum) {
				loyal_straight_flush = true;
				return 10;
			}
			//ストレートフラッシュを判定
			else {
				straight_flush = true;
				return 9 + 0.01 * firstnum;
				}
		}
		//フルハウスを判定
		if(3 == number_max) {
			three_card = true;
			for(int i = 1; i < number.length; i++) {
				if(3 == number[i]) {
					drawcase1 = i;
				}
				if(2 == number[i]) {
					full_house = true;
					drawcase2 = i;
				}
			}
			if(full_house) {
				return 7 + 0.01 * drawcase1 + 0.0001 * drawcase2;
			}
		}
		//フラッシュを判定
		if(5 == suit_max) {
			for(int i =0; i <number.length; i++) {
				if(1 <= number[i]) {
					drawcase1 = i;
				}
			}
			flush = true;
			return 6 + 0.01 * drawcase1;
		}
		//ストレートを判定
		if(loyal_straight_flush == false && straight_flush == false && straight) {
			for(int i = 13; drawcase1 != 0; i--) {
				if(i < firstnum -1 && firstnum + 3 < i) {
					if(number[i] == 1) {
						drawcase1 = i;
					}
				}
			}
			return 5 + 0.01 * firstnum + 0.0001 * drawcase1;
		}
		if(three_card && full_house == false) {
			for(int i = 1; i < number.length; i++) {
				if(3 == number[i]) {
					drawcase1 = i;
				}
				if(1 == number[i]) {
					drawcase2 = i;
				}
			}
			return 4 + 0.01 * drawcase1 + 0.0001 * drawcase2;
		}
		if(2 == pair_num) {
			two_pair = true;
			for(int i = 1; i < number.length; i++) {
				if(2 == number[i]) {
					if(drawcase2 == 0) {
						drawcase2 = i;
					}
					else {
						drawcase1 = i;
					}
				}
				if(1 == number[1]) {
					drawcase3 = i;
				}
			}
			return 3 + 0.01 * drawcase1 + 0.0001 * drawcase2 + 0.000001 * drawcase3;
		}
		if(1 == pair_num) {
			for(int i = 1; i < number.length; i++) {
				if(2 == number[i]) {
					drawcase1 = i;
				}
				if(1 == number[1]) {
					drawcase2 = i;
				}
			}
			return 2 + 0.01 * drawcase1 + 0.0001 * drawcase2;
		}
		for(int i = 0; i < number.length ; i++) {
			if(1 == number[i]) {
				drawcase1 = i ;
			}
		}
		return 1 + 0.01 * drawcase1;
	}
}

class Judge {
	static String person [] = {Player.member[0], Player.member[1], Player.member[2], Player.member[3]};
	static double[]point = new double[4];
	double max = 0;
	String bettle_result;
	public Judge (double P1, double C1, double C2, double C3 ) {
		bettle_result = JudgeMethod(P1, C1, C2, C3 );
	}

	public String JudgeMethod(double P1, double C1, double C2, double C3) {
				String temp1 = null;
				if(Chip1.drop == 1) {
					point[0] = 0;
				}
				else {
					point[0] = P1;
				}
				point[1] = C1;
				point[2] = C2;
				point[3] = C3;
				double temp = 0;
		for ( int i = 0; i < point.length - 1; i++ ) {
		    for ( int j = point.length - 1; j > i; j-- ) {
		      if ( point[j - 1] < point[j] ) {
		        temp = point[j - 1];
		        temp1 = person[j - 1];
		        point[j - 1] = point[j];
		        person[j - 1] = person[j];
		        point[j] = temp;
		        person[j] = temp1;
		      }
		    }
		  }
		if(point[0] ==point[1] && point[1] == point[2] && point[2] == point[3]) {
			return "引き分けです";
		}
		else if(point[0] ==point[1] && point[1] == point[2]) {
			return person[0] + "と" + person[1] + "と" + person[2] + "の勝ちです";
		}
		else if(point[0] ==point[1]) {
			return person[0] + "と" + person[1] + "の勝ちです";
		}
		else {
			return person[0] + "の勝ちです";
		}
	}
}

class ChipMove {
	public void chipMove(){	//勝者にチップを配る
		int winnerCheck1 = Player.randum + 1;
		int winnerCheck2 = winnerCheck1%4;		//親の次の人は誰か調べる
		if(Judge.point[0] ==Judge.point[1] && Judge.point[1] == Judge.point[2] && Judge.point[2] == Judge.point[3]) {	//引き分けの場合
			while(Player.chipSum != 0) {
				if(winnerCheck2 == 0) {
					if(Player.member[0]==Judge.person[0] || Player.member[0]==Judge.person[1] || Player.member[0]==Judge.person[2] || Player.member[0]==Judge.person[3]) {
						Player.pChip++;
						Player.chipSum--;
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}else {
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}
				}else if(winnerCheck2 == 1) {
					if(Player.member[1]==Judge.person[0] || Player.member[1]==Judge.person[1] || Player.member[1]==Judge.person[2] || Player.member[1]==Judge.person[3]) {
						Player.c1Chip++;
						Player.chipSum--;
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}else {
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}
				}else if(winnerCheck2 == 2) {
					if(Player.member[2]==Judge.person[0] || Player.member[2]==Judge.person[1] || Player.member[2]==Judge.person[2] || Player.member[2]==Judge.person[3]) {
						Player.c2Chip++;
						Player.chipSum--;
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}else {
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}
				}else if(winnerCheck2 == 3) {
					if(Player.member[3]==Judge.person[0] || Player.member[3]==Judge.person[1] || Player.member[3]==Judge.person[2] || Player.member[3]==Judge.person[3]) {
						Player.c3Chip++;
						Player.chipSum--;
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}else {
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}
				}
			}
		}else if(Judge.point[0] == Judge.point[1] && Judge.point[1] == Judge.point[2]) {	//勝者が3人の場合
			while(Player.chipSum != 0) {
				if(winnerCheck2 == 0) {
					if(Player.member[0]==Judge.person[0] || Player.member[0]==Judge.person[1] || Player.member[0]==Judge.person[2]) {
						Player.pChip++;
						Player.chipSum--;
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}else {
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}
				}else if(winnerCheck2 == 1) {
					if(Player.member[1]==Judge.person[0] || Player.member[1]==Judge.person[1] || Player.member[1]==Judge.person[2]) {
						Player.c1Chip++;
						Player.chipSum--;
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}else {
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}
				}else if(winnerCheck2 == 2) {
					if(Player.member[2]==Judge.person[0] || Player.member[2]==Judge.person[1] || Player.member[2]==Judge.person[2]) {
						Player.c2Chip++;
						Player.chipSum--;
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}else {
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}
				}else if(winnerCheck2 == 3) {
					if(Player.member[3]==Judge.person[0] || Player.member[3]==Judge.person[1] || Player.member[3]==Judge.person[2]) {
						Player.c3Chip++;
						Player.chipSum--;
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}else {
						winnerCheck1++;
						winnerCheck2 = winnerCheck1%4;
					}
				}
			}
	}else if(Judge.point[0] == Judge.point[1]) {	//勝者が2人の場合
		while(Player.chipSum != 0) {
			if(winnerCheck2 == 0) {
				if(Player.member[0]==Judge.person[0] || Player.member[0]==Judge.person[1]) {
					Player.pChip++;
					Player.chipSum--;
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}else {
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}
			}else if(winnerCheck2 == 1) {
				if(Player.member[1]==Judge.person[0] || Player.member[1]==Judge.person[1]) {
					Player.c1Chip++;
					Player.chipSum--;
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}else {
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}
			}else if(winnerCheck2 == 2) {
				if(Player.member[2]==Judge.person[0] || Player.member[2]==Judge.person[1]) {
					Player.c2Chip++;
					Player.chipSum--;
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}else {
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}
			}else if(winnerCheck2 == 3) {
				if(Player.member[3]==Judge.person[0] || Player.member[3]==Judge.person[1]) {
					Player.c3Chip++;
					Player.chipSum--;
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}else {
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}
			}
		}
	}else{
		while(Player.chipSum != 0) {	//勝者が1人の場合
			if(winnerCheck2 == 0) {
				if(Player.member[0]==Judge.person[0]) {
					Player.pChip++;
					Player.chipSum--;
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}else {
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}
			}else if(winnerCheck2 == 1) {
				if(Player.member[1]==Judge.person[0]) {
					Player.c1Chip++;
					Player.chipSum--;
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}else {
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}
			}else if(winnerCheck2 == 2) {
				if(Player.member[2]==Judge.person[0]) {
					Player.c2Chip++;
					Player.chipSum--;
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}else {
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}
			}else if(winnerCheck2 == 3) {
				if(Player.member[3]==Judge.person[0]) {
					Player.c3Chip++;
					Player.chipSum--;
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}else {
					winnerCheck1++;
					winnerCheck2 = winnerCheck1%4;
				}
			}
		}
	}
	Player.pBet = 0;
	Player.c1Bet = 0;
	Player.c2Bet = 0;
	Player.c3Bet = 0;
	System.out.println("Playerのチップの所持数は"+Player.pChip+"枚");
	System.out.println("CPU1のチップの所持数は"+Player.c1Chip+"枚");
	System.out.println("CPU2のチップの所持数は"+Player.c2Chip+"枚");
	System.out.println("CPU3のチップの所持数は"+Player.c3Chip+"枚");
	}
}

public class Poker {
	public static void main(String []args) {
		Player p = new Player();
		ChipMove cm = new ChipMove();
		Exchange ex = new Exchange();
		CPUBet cb = new CPUBet();
		int input;	//入力された値
		int[]ratingChip = new int[4];
		int[]ratingCheck = {0,0,0,0};
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("参加者は");
		for(int i=0;i<Player.member.length;i++) {	//参加者を表示
			System.out.print(Player.member[i]);
			if(i<Player.member.length-1) {
				System.out.print(",");
			}
		}
		System.out.println("の"+Player.member.length+"人です。");
		System.out.println("最初の所持チップは全員20枚です");
		boolean continueCheck1 = true;
		while(continueCheck1) {
			System.out.println("今回の親は"+Player.dealer+"です。");
			System.out.println("ゲームを開始します。");
			Deal.deal();	//手札を配る
			for(int i = 0 ; i < 5; i++) {
				Player.pHand[i] = Deal.mycard[i];
				Player.c1Hand[i] = Deal.CP1card[i];
				Player.c2Hand[i] = Deal.CP2card[i];
				Player.c3Hand[i] = Deal.CP3card[i];
			}

			System.out.println("まずチップ1枚をかけてください。");	//全員にチップを1枚賭けさせる
			Player.pBet++;
			Player.pChip--;
			Player.c1Bet++;
			Player.c1Chip--;
			Player.c2Bet++;
			Player.c2Chip--;
			Player.c3Bet++;
			Player.c3Chip--;
			System.out.println("チップを追加してください。");
			System.out.println("それぞれ合計4枚まで賭けられます。");
			while(CPUBet.callTime != 4) {	//チップを賭ける
				if(CPUBet.dealerNext2 == 0) {
					if(Chip1.drop == 0) {	//Playerがドロップしていない場合
						Chip1 c1 = new Chip1();
						if(Chip1.drop == 0) {
							cb.chipBet();
						}else {
							CPUBet.callTime++;
							CPUBet.dealerNext1++;
							CPUBet.dealerNext2 = CPUBet.dealerNext1 % 4;
						}
					}else {		//Playerがドロップしている場合
						CPUBet.callTime++;
						CPUBet.dealerNext1++;
						CPUBet.dealerNext2 = CPUBet.dealerNext1 % 4;
					}
				}else {	//CPU1,CPU2,CPU3
					cb.chipBet();
				}
			}
			cb.starterChange();
			try {
				ex.exchange();		//手札を交換
				Player.pHand[0] = Deal.mycard[0];
				Player.pHand[1] = Deal.mycard[1];
				Player.pHand[2] = Deal.mycard[2];
				Player.pHand[3] = Deal.mycard[3];
				Player.pHand[4] = Deal.mycard[4];
				System.out.println();
			}catch(IOException e){
				System.out.println("交換に失敗しました。");
			}
			System.out.println("チップを追加してください。");
			System.out.println("それぞれ合計4枚まで賭けられます。");
			while(CPUBet.callTime != 4) {	//賭けるチップの追加
				if(CPUBet.dealerNext2 == 0) {	//Player
					if(Chip1.drop == 0) {	//Playerがドロップしていない場合
						Chip1 c1 = new Chip1();
						if(Chip1.drop == 0) {
							cb.chipBet();
						}else {
							CPUBet.callTime++;
							CPUBet.dealerNext1++;
							CPUBet.dealerNext2 = CPUBet.dealerNext1 % 4;
						}
					}else {		//Playerがドロップしている場合
						CPUBet.callTime++;
						CPUBet.dealerNext1++;
						CPUBet.dealerNext2 = CPUBet.dealerNext1 % 4;
					}
				}else {	//CPU1,CPU2,CPU3
					cb.chipBet();
				}
			}
			HandJudge ex1 = new HandJudge(Player.pHand,Player.member[0]);
			HandJudge ex2 = new HandJudge(Player.c1Hand,Player.member[1]);
			HandJudge ex3 = new HandJudge(Player.c2Hand,Player.member[2]);
			HandJudge ex4 = new HandJudge(Player.c3Hand,Player.member[3]);
			Judge ex0 = new Judge(ex1.result, ex2.result, ex3.result, ex4.result);	//勝者を決定
			Player.chipSum = Player.pBet + Player.c1Bet + Player.c2Bet + Player.c3Bet;
			System.out.println(ex0.bettle_result);
			cm.chipMove();	//チップの移動
			if(Player.pChip<4 || Player.c1Chip<4 || Player.c2Chip<4 || Player.c3Chip<4) {		//ゲームの続行
				continueCheck1 = false;
			}else {
				boolean continueCheck2 = true;
				while(continueCheck2){
					try {
						System.out.println("続ける場合は1、終わる場合は0を入力してください。");
						input = Integer.parseInt(buf.readLine());
						if(input==0) {	//0が入力された場合
							continueCheck1=false;
							continueCheck2=false;
						}else if(input==1){		//1が入力された場合
							p.dealerChange();	//親の変更
							cb.reset();			//変更した値をリセット
							Judge.person[0]=Player.member[0];
							Judge.person[1]=Player.member[1];
							Judge.person[2]=Player.member[2];
							Judge.person[3]=Player.member[3];
							Chip1.drop = 0;
							Deal.dealcount=0;
							Deal.cardlist.clear();
							continueCheck2=false;
						}else {		//0と1以外の整数が入力された場合
							System.out.println("0か1を入力してください。");
						}
					}catch(Exception e) {	//小数や文字列が入力された場合
						System.out.println("決められた値を入力してください");
					}
				}
			}
		}
		ratingChip[0] = Player.pChip;
		ratingChip[1] = Player.c1Chip;
		ratingChip[2] = Player.c2Chip;
		ratingChip[3] = Player.c3Chip;
		Arrays.sort(ratingChip);
		try {
			String output = "result.txt";
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			writer.write("チップの所持数は・・・");	//所持チップを出力
			writer.newLine();
			writer.write("プレーヤー："+Player.pChip);
			writer.newLine();
			writer.write("CPU1："+Player.c1Chip);
			writer.newLine();
			writer.write("CPU2："+Player.c2Chip);
			writer.newLine();
			writer.write("CPU3："+Player.c3Chip);
			writer.newLine();
			writer.newLine();
			for(int i=Player.member.length;i>0;i--) {	//それぞれの順位を出力
				if(Player.pChip == ratingChip[i-1] && ratingCheck[0] == 0) {
					writer.write((5-i)+"位 "+Player.member[0]+" ");
					ratingCheck[0] = 1;
				}
				if(Player.c1Chip == ratingChip[i-1] && ratingCheck[1] == 0) {
					writer.write((5-i)+"位 "+Player.member[1]+" ");
					ratingCheck[1] = 1;
				}
				if(Player.c2Chip == ratingChip[i-1] && ratingCheck[2] == 0) {
					writer.write((5-i)+"位 "+Player.member[2]+" ");
					ratingCheck[2] = 1;
				}
				if(Player.c3Chip == ratingChip[i-1] && ratingCheck[3] == 0){
					writer.write((5-i)+"位 "+Player.member[3]+" ");
					ratingCheck[3] = 1;
				}
			}
			writer.close();
		}catch(IOException e) {
			System.out.println("ファイル出力に問題が生じました");
		}
		System.out.println("今回の結果を出力しました。");
		System.out.println("ゲームを終了します。");
	}
}
