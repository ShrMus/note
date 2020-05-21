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

## 3.1 add(E e)

将指定的元素追加到此数组的末尾。

思路：

1. 首先确保数组的容量是否足够（容量不够将对数组扩容，怎么扩容在后面的grow方法中）
2. 容量够的话直接在数组最后一个元素后面再添加一个元素
3. 记录数组元素的个数

```java
    /**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list 被追加到列表中的元素
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);  
        elementData[size++] = e;
        return true;
    }
```

这段文字说明下面的三个方法：这三个方法不仅仅提供给添加一个元素的方法调用，添加多个元素的方法需要扩容也用到了下面的三个方法。因为有添加多个元素的需求，如果数组需要扩容的话需要知道有多大容量的数组才能存放原数组中的元素和指定添加的元素。所以需要计算扩容后的数组容量。

例如原数组中有10个元素，需要添加7个元素，数组容量不够，那向内存申请一个容量为17的数组不就够了，先保留这个想法，但是我们知道申请的数组容量至少是17。

```java
    /**
     * 确保内部容量
     * @param minCapacity 数组最小容量
     */
    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }
```

```java
    /**
     * 计算容量
     * @Param elementData 数组缓冲区
     * @Param minCapacity 数组最小容量
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
     * @Param minCapacity 数组最小容量
     */
    private void ensureExplicitCapacity(int minCapacity) {
        modCount++; // AbstractList中的字段，记录列表被修改的次数，包括增，删，改等方法，初始值为0

        // overflow-conscious code
        // elementData.length表示数组的长度（容量）
        // size表示数组元素的个数
        if (minCapacity - elementData.length > 0)  
            grow(minCapacity);
    }
```

这段文字说明grow方法：此方法是在数组容量不足够再添加元素的时候对数组进行扩容。

1. 首先记录原数组中的元素个数
2. 将原数组扩容1.5倍，取地板值。例如原数组容量是10，扩容后的数组容量是15。原数组容量是3，扩容后的数组容量是4。原数组容量是7，扩容后的数组容量是10。
3. 如果扩容后的容量还是不够，就将扩容到所需的最小数组容量。前面的例子中容量为10的数组添加7个元素，最小数组容量是17，按照下面方法中的计算，容量为10的数组扩容后是15，容量还是不够，那么数组将扩容到17。
4. 如果最小数组容量达到了比MAX_ARRAY_SIZE还大的情况下，就将扩容到Integer.MAX_VALUE。
5. 然后是申请一个容量为newCapacity的数组，将原数组中的元素复制到新数组中。

oldCapacity是原数组的容量，newCapacity是扩容后数组的容量。

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
        if (newCapacity - minCapacity < 0)  // 扩容1.5倍还不够就取所需最小容量值
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)  // int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8; MAX_ARRAY_SIZE值为2147483639
            newCapacity = hugeCapacity(minCapacity);  // 比MAX_ARRAY_SIZE还大就取Integer.MAX_VALUE 值为2147483647
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
```

```java
    /**
     * 增加容量
     * @param minCapacity 数组所需的最小容量
     */
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
```

## 3.2 add(int index, E element)

在此数组中的指定位置插入指定元素。

例如有list[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

执行list.add(2,10);

结果list[0, 1, 10, 2, 3, 4, 5, 6, 7, 8, 9]

思路：

1. 先检查index的值是否合法
2. 确保数组的容量是否足够（容量不够怎么扩容前面已说明）
3. 容量够的话，将index以及后面的元素都往后移一位
5. 赋值，elementData[index] = element;
6. 记录数组元素的个数

```java
    /**
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
     * A version of rangeCheck used by add and addAll.
     */
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
```

## 3.3 addAll(Collection<? extends E> c)

将指定集合添加到列表末尾。

思路和add(E e)类似。

```java
    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the
     * specified collection's Iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified collection is this list, and this
     * list is nonempty.)
     *
     * @param c collection containing elements to be added to this list 要添加到此列表中的元素的集合
     * @return <tt>true</tt> if this list changed as a result of the call 如果添加的不是空集合返回true
     * @throws NullPointerException if the specified collection is null
     */
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount
        System.arraycopy(a, 0, elementData, size, numNew);  // 追加到elementData数组末尾
        size += numNew;
        return numNew != 0;
    }
```

## 3.4 addAll(int index, Collection<? extends E> c)

从指定位置开始，将指定集合中的所有元素插入此列表。

例如有list[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

c[10,11,12]

执行list.addAll(8,c);

结果list[0, 1, 2, 3, 4, 5, 6, 7, 10, 11, 12, 8, 9]

思路也和前面的类似，不过多个numMoved变量，这个变量用来判断是否需要移动数组中的元素，以及移动多少位。

如果numMoved == 0，表示追加到末尾。

如果numMoved > 0，表示需要将原数组中index开始以及后面的元素（numMoved个元素）都往后移动c的元素个数位（numNew位）。

```java
    /**
     * Inserts all of the elements in the specified collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified collection's iterator.
     *
     * @param index index at which to insert the first element from the
     *              specified collection 指定集合中要插入的索引
     * @param c collection containing elements to be added to this list 要添加到此列表中的元素的集合
     * @return <tt>true</tt> if this list changed as a result of the call 如果添加的不是空集合返回true
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException if the specified collection is null
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);

        Object[] a = c.toArray();
        int numNew = a.length;  // 指定集合的元素个数
        ensureCapacityInternal(size + numNew);  // Increments modCount

        int numMoved = size - index;  // 要复制的数组元素的数量
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                             numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }
```







