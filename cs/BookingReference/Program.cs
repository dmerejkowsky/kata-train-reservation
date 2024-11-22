var counter = new Counter();
var builder = WebApplication.CreateBuilder(args);
builder.WebHost.UseUrls("http://127.0.0.1:8082");
var app = builder.Build();

app.MapGet("/", () => "Hello World!");

app.MapGet("/booking_reference", () =>
{
    counter.Increment();
    return counter.Value;
}
);

app.Run();


class Counter
{
    private int _count = 123456789;

    public void Increment()
    {
        _count += 1;
    }

    public int Value => _count;
}

