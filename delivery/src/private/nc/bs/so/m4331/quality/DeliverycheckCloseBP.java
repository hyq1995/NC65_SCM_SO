package nc.bs.so.m4331.quality;

import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.so.m4331.entity.DeliveryCheckVO;

/**
 * 发货单质检信息关闭
 * 
 * @since 6.0
 * @version 2011-3-2 上午10:17:40
 * @author 祝会征
 */
public class DeliverycheckCloseBP {
  public void close(DeliveryCheckVO[] vos) {
    if (null == vos ||vos.length==0) {
      return;
    }
    this.setState(vos);
    this.updateDB(vos);
  }

  private void updateDB(DeliveryCheckVO[] vos) {
    VOUpdate<DeliveryCheckVO> update = new VOUpdate<DeliveryCheckVO>();
    String[] names = new String[] {
      DeliveryCheckVO.BOUTENDFLAG, DeliveryCheckVO.NTOTALOUTNUM
    };
    update.update(vos, names);
  }

  private void setState(DeliveryCheckVO[] vos) {
    for (DeliveryCheckVO vo : vos) {
      vo.setBoutendflag(UFBoolean.TRUE);
    }
  }
}
