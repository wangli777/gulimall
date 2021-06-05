<!--  -->
<template>
  <div>
    <el-switch
      v-model="draggable"
      active-text="开启拖拽"
      inactive-text="关闭拖拽"
    >
    </el-switch>
    <el-button v-if="draggable" @click="batchSave">批量保存</el-button>
    <el-button type="danger" @click="batchDelete">批量删除</el-button>

    <el-tree
      :data="menus"
      :props="defaultProps"
      :expand-on-click-node="false"
      :default-expanded-keys="defaultExpanded"
      show-checkbox
      node-key="catId"
      :draggable="draggable"
      :allow-drop="allowDrop"
      @node-drop="handleDrop"
      ref="menuTree"
    >
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level < 3"
            type="text"
            size="mini"
            @click="() => append(data)"
          >
            增加
          </el-button>
          <el-button type="text" size="mini" @click="edit(data)">
            修改
          </el-button>
          <el-button
            v-if="node.childNodes.length == 0"
            type="text"
            size="mini"
            @click="() => remove(node, data)"
          >
            删除
          </el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="30%"
      :close-on-click-modal="false"
    >
      <el-form :model="category">
        <el-form-item label="分类名称">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="计量单位">
          <el-input
            v-model="category.productUnit"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitData()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

export default {
  data() {
    return {
      pCid: [],
      draggable: false,
      updateNodes: [], //所有要修改的节点
      maxLevel: 0, //节点最大深度
      dialogTitle: "", //对话框的标题
      dialogType: "", //修改的对话框 edit or 添加的对话框 add
      menus: [], // Tree中的数据
      defaultProps: {
        children: "children", //指定子节点是哪个属性
        label: "name", //节点所展示的信息是哪个属性
      },
      defaultExpanded: [], //默认展开的节点
      dialogVisible: false, //是否显示对话框
      category: {
        name: "",
        parentCid: 0,
        catLevel: 0,
        showStatus: 1,
        sort: 0,
        catId: null,
        icon: "",
        productUnit: "",
      },
    };
  },
  methods: {
    getMenus() {
      this.$http({
        url: this.$http.adornUrl("/product/category/list/tree"),
        method: "get",
      }).then(({data}) => {
        // console.log("成功获取到菜单数据", data.data);
        this.menus = data.data;
      });
    },
    batchDelete() {
      let checkedNodes = this.$refs.menuTree.getCheckedNodes();
      let catIds = [];
      let catNames = [];
      console.log("被选中的节点：", checkedNodes);
      for (let i = 0; i < checkedNodes.length; i++) {
        const el = checkedNodes[i];
        catIds.push(el.catId);
        catNames.push(el.name);
        this.$confirm(`是否确认删除菜单【${catNames}】?`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            this.$http({
              url: this.$http.adornUrl("/product/category/delete"),
              method: "post",
              data: this.$http.adornData(catIds, false),
            }).then(({data}) => {
              this.$message({
                message: "菜单批量删除成功",
                type: "success",
              });
              // 刷新数据
              this.getMenus();
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
      }
    },
    batchSave() {
      this.$http({
        url: this.$http.adornUrl("/product/category/update/sort"),
        method: "post",
        data: this.$http.adornData(this.updateNodes, false),
      }).then(({data}) => {
        this.$message({
          message: "菜单顺序修改成功",
          type: "success",
        });
        // 刷新数据
        this.getMenus();
        // 展开当前节点
        this.defaultExpanded = this.pCid;
        //清空数据
        this.updateNodes = [];
        this.maxLevel = 0;
        // this.pCid = [];
      });
    },
    handleDrop(draggingNode, dropNode, dropType) {
      console.log("handleDrop: ", draggingNode, dropNode, dropType);
      // 1、当前节点最新的父节点id
      let pCid = 0;
      // 当前最新的兄弟节点
      let broNodes = null;
      if (dropType == "before" || dropType == "after") {
        pCid =
          dropNode.parent.data.catId == undefined
            ? 0
            : dropNode.parent.data.catId;
        broNodes = dropNode.parent.childNodes;
      } else {
        pCid = dropNode.data.catId;
        broNodes = dropNode.childNodes;
      }

      this.pCid.push(pCid);
      // 2、当前节点最新的顺序
      for (let i = 0; i < broNodes.length; i++) {
        if (broNodes[i].data.catId == draggingNode.data.catId) {
          //如果遍历的是当前拖拽的节点，还需要修改该节点的父id
          //如果当前节点的层级发生变化
          let catLevel = draggingNode.level;
          if (broNodes[i].level != draggingNode.level) {
            //当前节点的层级发生变化
            catLevel = broNodes[i].level;
            //当前节点的子节点层级也需要变化
            this.updateChildNodeLevel(broNodes[i]);
          }
          this.updateNodes.push({
            catId: broNodes[i].data.catId,
            sort: i,
            parentCid: pCid,
            catLevel: catLevel,
          });
        } else {
          this.updateNodes.push({catId: broNodes[i].data.catId, sort: i});
        }
      }
      // 3、当前节点最新层级
      console.log("this.updateNodes", this.updateNodes);
      // this.maxLevel = 0;
    },

    updateChildNodeLevel(node) {
      if (node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          var cNode = node.childNodes[i].data;
          this.updateNodes.push({
            catId: cNode.catId,
            catLevel: node.childNodes[i].level,
          });
          this.updateChildNodeLevel(node.childNodes[i]);
        }
      }
    },
    allowDrop(draggingNode, dropNode, type) {
      //1、被拖动的当前节点以及所在的父节点总层数不能大于3
      // console.log("allowDrop", draggingNode, dropNode, type);

      // 比原版改两处，allow开始前重置maxLevel和无子节点this.maxLevel=node.level
      this.maxLevel = 0;
      //计算当前节点的最大深度
      this.countNodeLevel(draggingNode);
      //找到被拖动节点的总层数 = (当前节点的最大深度 - 当前节点的层数)+1
      let deep = Math.abs(this.maxLevel - draggingNode.level) + 1;
      // let deep = this.maxLevel - draggingNode.level + 1;

      console.log("当前节点最大层数：", this.maxLevel);
      console.log("当前节点是第几层：", draggingNode.level);
      console.log("当前节点有的层数：", deep);
      console.log("被拖到的节点是第几层:", dropNode.level);
      console.log("被拖到的节点的父节点是第几层:", dropNode.parent.level);
      if (type == "inner") {
        console.log("inner总层数：", dropNode.level + deep);
        return deep + dropNode.level <= 3;
      } else {
        console.log("other总层数", dropNode.parent.level + deep);
        return deep + dropNode.parent.level <= 3;
      }
    },

    countNodeLevel(node) {
      //找到所有子节点，并求出最大深度
      if (node.childNodes != null && node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          if (node.childNodes[i].level > this.maxLevel) {
            this.maxLevel = node.childNodes[i].level;
          }
          this.countNodeLevel(node.childNodes[i]);
        }
      } else {
        //没有子节点，则最大层数即为该节点的层数
        this.maxLevel = node.level;
      }
    },
    submitData() {
      if (this.dialogType == "add") {
        this.addCategory();
      }
      if (this.dialogType == "edit") {
        this.editCategory();
      }
    },

    editCategory() {
      //结构表达式
      var {catId, name, icon, productUnit} = this.category;
      var data = {catId, name, icon, productUnit};
      this.$http({
        url: this.$http.adornUrl("/product/category/update"),
        method: "post",
        data: this.$http.adornData(data, false),
      }).then(({data}) => {
        this.$message({
          message: "菜单修改成功",
          type: "success",
        });
        //关闭对话框
        this.dialogVisible = false;
        //刷新数据
        this.getMenus();
        //打开当前添加的节点
        this.defaultExpanded = [this.category.parentCid];
      });
    },

    addCategory() {
      console.log("要提交的数据：", this.category);
      this.$http({
        url: this.$http.adornUrl("/product/category/save"),
        method: "post",
        data: this.$http.adornData(this.category, false),
      }).then(({data}) => {
        this.$message({
          message: "菜单添加成功",
          type: "success",
        });
        //关闭对话框
        this.dialogVisible = false;
        //刷新数据
        this.getMenus();
        //打开当前添加的节点
        this.defaultExpanded = [this.category.parentCid];
      });
    },
    //点击添加按钮
    append(data) {
      console.log("append", data);
      this.dialogType = "add";
      this.dialogTitle = "添加分类";
      this.dialogVisible = true;
      this.category.parentCid = data.catId;
      this.category.catLevel = data.catLevel * 1 + 1;

      this.category.catId = null;
      this.category.name = "";
      this.category.icon = "";
      this.category.productUnit = "";
      this.category.sort = 0;
      this.category.showStatus = 1;
      // this.category.showStatus = data.catId;
      // this.category.sort = data.catId;
    },
    //点击修改按钮
    edit(data) {
      console.log("edit", data);
      this.dialogType = "edit";
      this.dialogTitle = "修改分类";
      this.dialogVisible = true;

      //发送请求获取当前节点最新的数据
      this.$http({
        url: this.$http.adornUrl(`/product/category/info/${data.catId}`),
        method: "get",
      }).then(({data}) => {
        console.log("回显的数据", data.data);
        this.category.catId = data.data.catId;
        this.category.name = data.data.name;
        this.category.icon = data.data.icon;
        this.category.productUnit = data.data.productUnit;
        this.category.parentCid = data.data.parentCid;
      });
    },

    remove(node, data) {
      console.log("remove", node, data);
      var ids = [data.catId];
      this.$confirm(`是否确认删除菜单【${data.name}】?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$http({
            url: this.$http.adornUrl("/product/category/delete"),
            method: "post",
            data: this.$http.adornData(ids, false),
          }).then(({data}) => {
            this.$message({
              message: "菜单删除成功",
              type: "success",
            });
            // 刷新数据
            this.getMenus();
            // 展开当前节点
            this.defaultExpanded = [node.parent.data.catId];
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },

    // allowDrag(draggingNode) {
    //   return draggingNode.data.label.indexOf("三级 3-2-2") === -1;
    // },
  },
  //import引入的组件需要注入到对象中才能使用
  components: {},

  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},

  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.getMenus();
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
  },
  beforeCreate() {
  }, //生命周期 - 创建之前
  beforeMount() {
  }, //生命周期 - 挂载之前
  beforeUpdate() {
  }, //生命周期 - 更新之前
  updated() {
  }, //生命周期 - 更新之后
  beforeDestroy() {
  }, //生命周期 - 销毁之前
  destroyed() {
  }, //生命周期 - 销毁完成
  activated() {
  }, //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style scoped>
/*@import url(); 引入公共css类*/
</style>
