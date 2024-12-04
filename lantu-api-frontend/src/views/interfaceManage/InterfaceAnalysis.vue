<template>
  <div style="padding: 20px">
    <a-row :gutter="20">
      <a-col :span="8">
        <a-card title="调用次数最多的接口TOP3">
          <div ref="container" id="container" ></div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import {Chart} from '@antv/g2';
import {onMounted, ref} from "vue";
import {listTopInvokeInterfaceInfo} from "../../api/analysis";
import {Message} from "@arco-design/web-vue";

// 准备数据
const data = ref();
const getData = async () => {
  try {
    const res = await listTopInvokeInterfaceInfo()
    if (res.code === 0) {
      data.value = res.data
    } else {
      Message.error(res.message)
    }
  } catch (error) {
    Message.error(error.response?.data?.message || "系统错误")
  }
}
onMounted(async () => {
  await getData()
  const chart = new Chart({
    container: 'container',
    autoFit: true,
  });

  chart.coordinate({ type: 'theta', outerRadius: 0.6 });

  chart
      .interval()
      .data(data.value)
      .transform({ type: 'stackY' })
      .encode('y', 'totalNum')
      .encode('color', 'name')
      .legend('color', { position: 'bottom', layout: { justifyContent: 'center' } })
      .label({
        position: 'outside',
        text: (data) => `${data.name}`,
      })
      .tooltip((data) => ({
        name: data.name,
        value: `共调用 ${data.totalNum} 次`,
      }));

  await chart.render();
})


</script>

<style scoped lang="scss">

</style>
