> 【更新中】本文讲解ArrayList源码中的常用方法和应用场景。

[TOC]

# 1. 元数据

先看看ArrayList提供的元数据，如下：

```java
/**
 * ArrayList是一个实现了List接口的动态数组（Resizable-array）。
 *
 */
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
    
    private static final long serialVersionUID = 8683452581122892189L;

    /**
     * 默认初始化容量。
     * Default initial capacity. 
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 用于空实例的共享空数组实例。
     * Shared empty array instance used for empty instances. 
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * 共享空数组实例，用于默认大小的空实例。
     * 我们将其与EMPTY_ELEMENTDATA区分开来，以了解添加第一个元素时的膨胀程度。
     * Shared empty array instance used for default sized empty instances. We
     * distinguish this from EMPTY_ELEMENTDATA to know how much to inflate when
     * first element is added.
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * 存储ArrayList元素的数组缓冲区。
     * ArrayList的容量是此数组缓冲区的长度。
     * 添加第一个元素时，elementData==DEFAULTCAPACITY_EMPTY_ELEMENTDATA的任何空ArrayList都将扩展为DEFAULT_CAPACITY。
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     */
    transient Object[] elementData; // non-private to simplify nested class access

    /**
     * ArrayList的大小(它包含的元素的数量)。
     * The size of the ArrayList (the number of elements it contains).
     *
     * @serial
     */
    private int size;
}
```

# 2. 构造方法

ArrayList共提供了3个构造方法，先看空参构造方法。

## 2.1 无参构造方法

```java
    /** 
     * 构造一个初始容量为10的空列表。
     * Constructs an empty list with an initial capacity of ten.
     */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }
```

初始容量为10是哪来的，元数据DEFAULT_CAPACITY的初始值为10。

## 2.2 有参构造方法

```java
    /**
     * 构造具有指定初始容量的空列表。
     * Constructs an empty list with the specified initial capacity.
     *
     * @param  initialCapacity  the initial capacity of the list 初始容量列表的初始容量
     * @throws IllegalArgumentException if the specified initial capacity is negative 如果指定初始容量是负的
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }
```

上面的代码中是判断参数initialCapacity的值范围，合法再进行初始化。看下一个构造方法。

```java
    /**
     * 构造包含指定集合的元素的列表，按集合的迭代器返回这些元素的顺序。
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this list 要将其元素放入此列表中的集合
     * @throws NullPointerException if the specified collection is null 如果指定的集合为空
     */
    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();  // 返回包含集合c中所有元素的数组。
        if ((size = elementData.length) != 0) {
            // c.toArray方法可能不会返回Object[]
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }
```

上面的构造方法注释```c.toArray might (incorrectly) not return Object[] (see 6260652)```，参考[https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6260652](https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6260652)

从这3个构造方法可以看到元素都是存放在elementData中的。

# 3. 常用方法

## 3.1 新增

```java
    /**
     * 在此列表中的指定位置插入指定元素。
     * 将当前位于该位置的元素（如果有）和任何后续元素向右移动（将一个元素添加到其索引中）。
     * Inserts the specified element at the specified position in this
     * list. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted 要插入指定元素的索引
     * @param element element to be inserted 要插入的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void add(int index, E element) {
        rangeCheckForAdd(index);  // 检查索引的合法性

        ensureCapacityInternal(size + 1);  // 总容量要加1
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        elementData[index] = element;
        size++;
    }
```

```java
    /**
     * 确保内部容量
     */
    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }
```

```java
    /**
     * 计算容量
     */
    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) { // 参考无参构造方法
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
    }
```

```java
    /**
     * 明确容量大小
     */
    private void ensureExplicitCapacity(int minCapacity) {
        modCount++; // AbstractList中的字段，记录列表被修改的次数，包括增，删，改等方法，初始值为0

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }
```

```java
    /**
     * 增加容量以确保它至少可以容纳由最小容量参数指定的元素数。
     * Increases the capacity to ensure that it can hold at least the
     * number of elements specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity 所需的最小容量
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;  // 原来的容量
        int newCapacity = oldCapacity + (oldCapacity >> 1);  // 新容量是原容量的1.5倍，取地板值
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
```



