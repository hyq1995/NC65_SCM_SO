package nc.ui.so.m33.manest.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.so.m33.maintain.m4c.ISaleOutETMaintain;
import nc.ui.pubapp.uif2app.view.ShowUpableBillListView;
import nc.ui.scmpub.action.SCMActionInitializer;
import nc.ui.so.m33.pub.SquareUIUtils;
import nc.ui.so.m33.pub.action.SquareCreditExceptionProcess;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.credit.exception.CreditCheckException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.SCMActionCode;
import nc.vo.so.m33.m4c.entity.SquareOutViewVO;
import nc.vo.trade.checkrule.VOChecker;

public class SquareEstAction extends NCAction {

  private static final long serialVersionUID = 3886582906403785888L;

  private ShowUpableBillListView listView;

  private AbstractAppModel model;

  public SquareEstAction() {
    SCMActionInitializer.initializeAction(this, SCMActionCode.SO_ESTIMATE);
  }

  @Override
  public void doAction(ActionEvent e) throws Exception {
    SquareOutViewVO[] vos = SquareUIUtils.getSelectVOs(this.listView);
    if (vos == null || vos.length == 0) {

      ExceptionUtils
          .wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("4006010_0", "04006010-0000")/*@res "无选中行"*/);
    }

    // 手工暂估应收
    ISaleOutETMaintain bo =
        NCLocator.getInstance().lookup(ISaleOutETMaintain.class);
    try {
      bo.manualEstimate(vos);
      // 刷新界面的显示
      SquareUIUtils.deleteVoAfterAction(this.listView);
    }
    catch (Exception e1) {
      if (e1 instanceof CreditCheckException) {
        new SquareCreditExceptionProcess()
            .processCreditCheckException((CreditCheckException) e1);
      }
      else {
        ExceptionUtils.wrappException(e1);
      }
    }

    // 显示成功信息
    ShowStatusBarMsgUtil.showStatusBarMsg(
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4006010_0",
            "04006010-0001")/*@res "暂估应收成功！"*/, this.getModel().getContext());
  }

  public ShowUpableBillListView getListView() {
    return this.listView;
  }

  public AbstractAppModel getModel() {
    return this.model;
  }

  public void setListView(ShowUpableBillListView listView1) {
    this.listView = listView1;
  }

  public void setModel(AbstractAppModel model1) {
    this.model = model1;
    this.model.addAppEventListener(this);
  }

  @Override
  protected boolean isActionEnable() {
    boolean flag = false;
    Object[] datas =
        this.getListView().getBillListPanel().getBodyBillModel()
            .getBodySelectedVOs(SquareOutViewVO.class.getName());
    if (datas != null && datas.length > 0) {
      if (!VOChecker.isEmpty(datas[0])) {
        SquareOutViewVO svo = (SquareOutViewVO) datas[0];
        flag = svo.ifCanET();
      }
    }
    return flag;
  }

}
