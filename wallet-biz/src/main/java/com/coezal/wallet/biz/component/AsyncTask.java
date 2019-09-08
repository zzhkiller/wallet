package com.coezal.wallet.biz.component;

import com.coezal.wallet.api.bean.FetchCash;
import com.coezal.wallet.api.bean.RechargeRequest;
import com.coezal.wallet.api.bean.TokenTransaction;
import com.coezal.wallet.api.bean.request.CheckFetchCashRequest;
import com.coezal.wallet.api.excetion.BizException;
import com.coezal.wallet.biz.service.NoticeService;
import com.coezal.wallet.biz.service.WalletTransactionListenerServiceImpl;
import com.coezal.wallet.biz.util.WalletUtils;
import com.coezal.wallet.dal.dao.FetchCashMapper;
import com.coezal.wallet.dal.dao.TokenTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.coezal.wallet.api.enums.ResultCode.FETCH_CASH_ERROR;

/**
 * Version 1.0
 * Created by lll on 2019-09-07.
 * Description
 * copyright generalray4239@gmail.com
 */

@Component
public class AsyncTask {

  private static final Logger logger  = LoggerFactory.getLogger("AsyncTask");

  @Resource
  NoticeService noticeService;

  @Resource
  TokenTransactionMapper tokenTransactionMapper;

  @Resource
  FetchCashMapper mapper;

  /**
   * 异步检查用户充值记录
   * @param userWalletAddress
   * @param tokenContractAddress
   * @param server  检查环境： test | official
   * @param userSign
   * @param checkCode
   */
  @Async
  public void checkUserRecharge(String userWalletAddress, String tokenContractAddress, String server, String userSign, String checkCode) {

    //2、查询服务器，获取充值数据
    WalletTransactionListenerServiceImpl impl = new WalletTransactionListenerServiceImpl();
    List<TokenTransaction> transactionList = impl.getTransactionByAddressAndTokenContractAddress(server, userWalletAddress, tokenContractAddress);
    if (transactionList != null && transactionList.size() > 0) { //
      for (TokenTransaction transaction : transactionList) {
        if (transaction.getToAddress().equals(userWalletAddress)) { //如果是转入
          try {
            transaction.setNotifySuccessFlag((byte) 0);
            tokenTransactionMapper.insert(transaction);
            boolean success = sendRechargeNotice(userSign, checkCode, userWalletAddress, transaction);
            if (success) {
              transaction.setNotifySuccessFlag((byte) 1);
              tokenTransactionMapper.update(transaction);//插入数据
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  private boolean sendRechargeNotice(String userSign, String checkCode, String userWalletAddress, TokenTransaction transaction) {
    RechargeRequest rechargeRequest = new RechargeRequest();
    rechargeRequest.setUsersign(userSign);
    rechargeRequest.setCheckcode(checkCode);
    rechargeRequest.setId(transaction.getHash());
    rechargeRequest.setTokenname(transaction.getTokenSymbol());
    rechargeRequest.setWallet(userWalletAddress);
    rechargeRequest.setTime(transaction.getTimeStamp());
    rechargeRequest.setMoney(WalletUtils.getMoney(transaction.getValue(), transaction.getTokenDecimal()));
    return noticeService.rechargeNotice(rechargeRequest);
  }


  /**
   * 异步提现
   * @param userSign
   * @param checkCode
   * @param id
   * @param cash
   */
  @Async
  public void doFetchCashRequest(String userSign, String checkCode, long id, FetchCash cash) {
    CheckFetchCashRequest request = new CheckFetchCashRequest();
    request.setUsersign(userSign);
    request.setCheckcode(checkCode);
    request.setId(id);
    boolean checkFetch = noticeService.checkFetchCash(request);
    if (checkFetch) { //校验通过，
      mapper.insert(cash);

    } else {
      throw new BizException(FETCH_CASH_ERROR);
    }
  }

  private void deleteToken(){
    TokenTransaction transaction = new TokenTransaction();
    transaction.setNotifySuccessFlag((byte)1);
    List<TokenTransaction> transactions = tokenTransactionMapper.select(transaction);
    for(TokenTransaction tt: transactions){
      tokenTransactionMapper.deleteById(tt.getId());
    }
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args){
    AsyncTask task = new AsyncTask();
    task.deleteToken();
  }

}
