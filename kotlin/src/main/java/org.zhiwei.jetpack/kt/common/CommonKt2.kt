package org.zhiwei.jetpack.kt.common

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2019年10月15日 17:44
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * Kotlin进阶语法的索引演示,
 * 1、基础补充，集合包含、map遍历、lambda、?符号、let、with、also等
 * 2、函数继承时候，代码执行顺序，构造函数先实例化成员属性，然后是init代码块，然后是 类内的其他成员属性，然后是子类的，依次如此。如果有伴生对象object，会限制性object，因为它是静态的。
 * 3、
 */
private class CommonKt2 {

    //<editor-folder desc="1、基础补充">

    //1、in 可用于集合判断实例是否属于集合/数组
    val array = arrayOf("adga", "dggd", "A", "C", 2, false, 'c', 0b0101, 0x33, 99L, 18.0f)

    private fun testIn() {
        when {
            "A" in array -> "A 在 array中"
            2 in array -> println("2 属于数组array")
        }
    }

    //2、lambda表达式 配合 链式操作
    private fun testL() {
        array.filter { it is String }//过滤string的元素出来，得到一个只有string元素的数组
            .sortedBy { (it as String) }//上面得到的数组都是string的元素，这里强转一下，自然排序
            .map { (it as String).toUpperCase() }//转为大写
            .forEach {
                println(it)
            }
    }

    //3，map元素遍历
    val map = mapOf<Int, String>(1 to "a", 2 to "B", 3 to "C")

    private fun testMap() {
        for ((k, v) in map) {
            println("$k,$v")
        }
    }

    //?表达式的if功能
    private fun testIf() {
        //? 表示 if not null
        val file: File? = null
        println(file?.name)
        //if not null and else 即 file.name为空的时候，就返回指定值，非空 则是自身
        file?.name ?: "defalut name"
        //if null
        file?.name ?: throw NullPointerException("自定义抛出NPL")
        //配合let函数，却表not null
        file?.let {
            //这里执行函数，就能确保it 是非null的，
            it.name

        }
        //补充，bool为Boolean?可null时候，判断的话，就要bool==ture，
        var a = 1
        var b = 3
        //交换a、b的值,以下函数的意思，就是 a= b，同时，让b = a，交换指针
        a = b.also { b = a }
    }

    //4、表达式，if、when、try..catch、都可以作为对象值
    private fun testExp() {
        //这里为了演示可能性，随意写了类型，具体根据业务
        val a = if (1 > 0) "ss" else 3
        val b = when {
            1 is Int -> 3
            "2" is String -> "dd"
            else -> 'c'
        }
        val c = try {
            File.createTempFile("", "")
        } catch (e: Exception) {
            e.printStackTrace()
            File("add")
        }

        //如果这些表达式用于一个函数 return值，也可以，还能更进一步简写 函数表达式
        fun simpleIt() = 3

        fun simp() = when {
            1 is Int -> 3
            "2" is String -> "dd"
            else -> 'c'
        }
    }

    //5、单个对象多次调用其函数,使用with操作符,这样可以在其内部多次调用函数，但是with调用函数执行后，得到的是最后一行函数的返回值
    private fun testWith() {
        //演示用
        val ctx: Context? = null
        val builder = AlertDialog.Builder(ctx)
        with(builder) {
            setTitle("")
            setMessage("")
            setView(null)
            //with 内函数依次执行，得到最后一行函数的返回值
            create()
        }
    }

    //6、类似Java7的try with
    @TargetApi(Build.VERSION_CODES.O)
    private fun testTry() {
        //这里没有try catch，隐式的处理了File 的IO异常
        val ins = Files.newInputStream(Paths.get("file path"))
        ins.buffered(1024).reader(Charsets.UTF_8).use { reader ->
            println(reader.readText())

        }
    }

    //7、泛型函数支持泛型信息,这里就不引用Gson包了，下面的函数，就是json解析为对象的函数
//    private inline fun <reified T:Any> Gson.fromJson(json:JsonElement):T = this.fromJson(json,T::class.java)


    //8、== 与 === ，前者表示 值 判断，后者表示 指针判断，也就是对象实例的判断
    private data class CC(val a: Int)

    private val s1 = CC(2)
    private val s2 = CC(2)
    private fun testS() {
        println(s1 == s2)//都是 2 ，所以true
        println(s2 === s2)//并不是同一个对象实例，所以 false
    }

    //9、类继承中的函数执行顺序，一般是先基类的init函数，然后构造函数，再是子类的init函数和构造函数，但是主构造函数不同,如果有主构造函数含有成员变量，会初始化成员变量
    private open class Base(val name: String) {

        init {
            println("Base的Init代码块")
        }

        open val size: Int =
            name.length.also { println("Base的成员属性，不是在构造函数内的: $it") }
    }

    private class Derived(
        name: String,
        val lastName: String
    ) : Base(name.capitalize().also { println("传给 Base 构造函数内的成员属性参数: $it") }) {

        init {
            println("Derived 的 init 代码块")
        }

        override val size: Int =
            (super.size + lastName.length).also { println("Derived 覆写 base的属性的执行 : $it") }
    }

    private fun testEx() {
        val dd = Derived("ddd", "eee")
        println(dd)
        //得到的结果是
        /*
传给 Base 构造函数内的成员属性参数: Agdd
Base的Init代码块
Base的成员属性，不是在构造函数内的: 4
Derived 的 init 代码块
Derived 覆写 base的属性的执行 : 7
org.zhiwei.jetpack.kt.ExampleUnitTest$Derived@156643d4
         */
        //原因是，代码执行顺序，构造函数先实例化成员属性，然后是init代码块，然后是 类内的其他成员属性，然后是子类的，依次如此。如果有伴生对象object，会限制性object，因为它是静态的。
    }

    //10、幕后字段 field 幕后属性 使用下划线开头，并私有化
    private var ssf: Int = 0
        //可以自己设置，比如 field = value + 100,那么set值的时候，就默认加100了
        set(value) {
            field = value
        }
        get() {
            return field
        }
    private var _pint: Int = 100//私有属性，在下面用作幕后属性
    private val publicInt: Int
        //对外属性，不支持修改，并且实际获取的是内部的_pint值，类似于 代理模式
        get() {
            return _pint
        }

    //</editor-folder>


}