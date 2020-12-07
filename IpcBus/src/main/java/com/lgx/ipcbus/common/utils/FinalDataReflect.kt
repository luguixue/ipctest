package com.lgx.ipcbus.common.utils

import android.os.Build
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.util.*
import java.util.stream.Collectors
import kotlin.jvm.Throws

/**
 * Description : todo
 * Created by LuGuiXue on 2020/12/1 16:25.
 */
public object FinalDataReflect {

    @Throws(Exception::class)
    fun getClassFinalData(clazz: Class<*>): List<Any>? {
        val fields = clazz.declaredFields
        val descriptorList =
            ArrayList<Any>()
        for (field in fields) {
            //打开权限
            field.isAccessible=true
            var descriptor = Modifier.toString(field.modifiers) //获得其属性的修饰
            descriptor = if ("".equals( descriptor)) "" else descriptor + ""
            val varargStr=field.get(clazz)
            var str= "$descriptor=${field.name}=$varargStr"
            if (varargStr is Array<*>) {
                str= "$descriptor=${field.name}=${varargStr[0]}=${varargStr[1]}"
            }
            descriptorList.add(str)
        }
        return descriptorList
    }

    /**
     * 获取类以及父类的属性类型
     * @param clazz
     * @return
     */
    @Throws(Exception::class)
    fun getAllFieldClazzs(clazz: Class<*>?): ArrayList<Class<*>>? {
        var clazz = clazz
        val classs =
            ArrayList<Class<*>>()
        var tempClasss: List<Class<*>>? = null
        while (clazz != Any::class.java) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tempClasss =
                    Arrays.asList(*clazz!!.declaredFields)
                        .stream().map { field: Field -> field.type }
                        .collect(Collectors.toList())
            }
            classs.addAll(tempClasss!!)
            clazz = clazz!!.superclass
        }
        return classs
    }

    /**
     * 获取类里面指定的属性类型，检测到list类型属性会自动获取其泛型
     * @param clazz
     * @param name
     * @return
     */
    fun getAllFieldClass(
        clazz: Class<*>?,
        name: String?
    ): Class<*>? {
        var clazz = clazz
        var field: Field? = null
        while (clazz != Any::class.java) {
            try {
                field = clazz!!.getDeclaredField(name!!)
                break
            } catch (e: NoSuchFieldException) {
                clazz = clazz!!.superclass
            }
        }
        return if (field!!.type == MutableList::class.java) getListGenericClass(
            field
        ) else field.type
    }

    /**
     * 获取类里面指定的属性类型，检测到list类型属性会自动获取其泛型，并且可获取list泛型里面的指定属性
     * @param clazz
     * @param name
     * @return
     */
    fun getFieldClass(clazz: Class<*>?, name: String): Class<*>? {
        var clazz = clazz
        val nameList =
            Arrays.asList(*name.split("\\.".toRegex()).toTypedArray())
        for (tempName in nameList) {
            clazz = getAllFieldClass(clazz, tempName)
        }
        return clazz
    }

    /**
     * 获取list的泛型类所有属性
     * @param field
     * @return
     */
    fun getListGenericClass(field: Field?): Class<*>? {
        val listGenericType =
            field!!.genericType as ParameterizedType
        val listActualTypeArguments =
            listGenericType.actualTypeArguments
        return listActualTypeArguments[0] as Class<*>
    }

    /**
     * 获取list的泛型类指定属性
     * @param clazz
     * @param name
     * @return
     */
    fun getListGenericClass(
        clazz: Class<*>,
        name: String?
    ): Class<*>? {
        var listField: Field? = null
        try {
            listField = clazz.getDeclaredField(name!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val listGenericType =
            listField!!.genericType as ParameterizedType
        val listActualTypeArguments =
            listGenericType.actualTypeArguments
        return listActualTypeArguments[0] as Class<*>
    }












}