class MergeSort 
{
	public static void main(String[] args) 
	{
		int[] a={-2,22,37,0,1,-2,-5,95,82,64};
		a=ms(a);
		for (int i:a)
		System.out.print(" "+i+" ");
	}
	public static int[] ms(int[] a)
	{
		int size=a.length;
		if (size>1)
		{
			int[] b=new int[size/2];
			int[] c=new int[(int)Math.ceil(size/2.0)];
			System.arraycopy(a,0,b,0,size/2);
			System.arraycopy(a,size/2,c,0,(int)Math.ceil(size/2.0));
			b=ms(b);
			c=ms(c);
			a=merge(a,b,c);
		}
		return a;
	}

	public static int[] merge(int[]a,int[]b,int[]c)
	{
		int p=b.length, q=c.length;
		int i=0,j=0,k=0;
		while (i<p&&j<q)
		{
			if (b[i]<c[j])
			{
				a[k]=b[i];
				i++;
			}
			else
			{
				a[k]=c[j];
				j++;
			}

			k++;
		}
		if (i==p)		System.arraycopy(c,j,a,k,q-j);
		if (j==q)		System.arraycopy(b,i,a,k,p-i);
		return a;
		
	}
}
