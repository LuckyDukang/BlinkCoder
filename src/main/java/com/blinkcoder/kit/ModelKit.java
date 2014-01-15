package com.blinkcoder.kit;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import java.util.List;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 13-10-11
 * Time: 下午10:22
 */
public class ModelKit {
    private Model dao;
    private String cacheNameForOneModel;

    public ModelKit(Model dao, String cacheNameForOneModel) {
        this.dao = dao;
        this.cacheNameForOneModel = cacheNameForOneModel;
    }

    public <M> Page<M> loadModelPage(Page<M> page) {
        List<M> modelList = page.getList();
        for (int i = 0; i < modelList.size(); i++) {
            Model model = (Model) modelList.get(i);
            M obj = getModel(model.getInt("id"));
            modelList.set(i, obj);
        }
        return page;
    }

    public <M> List<M> loadModel(List<M> modelList) {
        for (int i = 0; i < modelList.size(); i++) {
            Model model = (Model) modelList.get(i);
            M obj = getModel(model.getInt("id"));
            modelList.set(i, obj);
        }
        return modelList;
    }

    public <M> M getModel(int id) {
        final int ID = id;
        final Model DAO = dao;
        return CacheKit.get(cacheNameForOneModel, ID, new IDataLoader() {
            @Override
            public Object load() {
                return DAO.findById(ID);
            }
        });
    }

}
